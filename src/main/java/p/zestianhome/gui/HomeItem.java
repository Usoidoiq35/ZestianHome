package p.zestianhome.gui;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import p.zestianhome.ZestianHome;
import p.zestianhome.managers.CooldownManager;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.sql.SQLException;

public class HomeItem extends AbstractItem implements Listener {
    private final ZestianHome plugin;

    private final String name;
    private final Location location;
    private boolean isTeleporting;

    public HomeItem(ZestianHome plugin, String name, Location location) {
        this.plugin = plugin;
        this.name = name;
        this.location = location;
        this.isTeleporting = false;
    }

    @Override
    public ItemProvider getItemProvider() {
        // Crear una instancia de ItemStack con el material PLAYER_HEAD
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);

        // Obtener el SkullMeta del ItemStack
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

        // Establecer el propietario de la cabeza del jugador
        skullMeta.setOwner("Spinnin34");

        // Establecer el nombre de la cabeza del jugador usando la variable name
        skullMeta.setDisplayName("§x§F§B§D§5§9§3" + name);

        // Aplicar el SkullMeta al ItemStack
        playerHead.setItemMeta(skullMeta);

        // Devolver el proveedor de ítems
        return new ItemBuilder(playerHead);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.equals(ClickType.DROP)) {
            try {
                plugin.getDatabase().removeHome(player, name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            player.sendMessage(plugin.getMessage("gui.home-deleted").replace("%name%", "§x§F§B§D§5§9§3" + name));
            player.closeInventory();
            return;
        }

        if (isTeleporting) {
            player.sendMessage(plugin.getMessage("gui.teleport-cooldown"));
            return;
        }

        isTeleporting = true;

        player.sendMessage(plugin.getMessage("gui.teleporting").replace("%name%", "§x§F§B§D§5§9§3" + name));

        // Esperar 3 segundos antes de teletransportar
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isTeleporting) {
                    return; // Si ya se canceló la acción, no hacer nada
                }

                player.teleport(location);
                player.sendMessage(plugin.getMessage("gui.teleported").replace("%name%", "§x§F§B§D§5§9§3" + name));
                isTeleporting = false;
            }
        }.runTaskLater(plugin, 60L); // 60 ticks = 3 segundos
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isTeleporting && player.equals(event.getPlayer())) {
            event.setCancelled(true);
            player.sendMessage(plugin.getMessage("gui.move-cancel"));
            isTeleporting = false;
        }
    }
}

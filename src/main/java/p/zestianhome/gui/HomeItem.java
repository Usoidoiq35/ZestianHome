package p.zestianhome.gui;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import p.zestianhome.ZestianHome;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.sql.SQLException;

public class HomeItem extends AbstractItem implements Listener {
    private final ZestianHome plugin;

    private final String name;
    private final Location location;
    private boolean isTeleporting;
    private BukkitTask teleportTask;

    public HomeItem(ZestianHome plugin, String name, Location location) {
        this.plugin = plugin;
        this.name = name;
        this.location = location;
        this.isTeleporting = false;
    }

    @Override
    public ItemProvider getItemProvider() {

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        skullMeta.setOwner("Spinnin34");
        skullMeta.setDisplayName("§x§F§B§D§5§9§3" + name);
        playerHead.setItemMeta(skullMeta);
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
            player.closeInventory();
            return;
        }

        startTeleportCooldown(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isTeleporting && player.equals(event.getPlayer())) {
            if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
                cancelTeleport(player);
            }
        }
    }

    // Correcciones en el método findSafeLocation para buscar una ubicación segura
    private Location findSafeLocation(Location location, Player player) {
        World world = location.getWorld();
        Block block = location.getBlock();

        if (block.getType() == Material.LAVA || block.getType() == Material.FIRE ||
                block.getLocation().getY() >= world.getMaxHeight() - 1 || block.getLocation().getY() <= 1) {
            player.sendMessage(ChatColor.RED + "¡La ubicación no es segura! Coordenadas: X=" + location.getBlockX() + ", Y=" + location.getBlockY() + ", Z=" + location.getBlockZ());
            return null;
        }

        if (block.getType().isSolid()) {
            for (int y = 1; y <= 3; y++) {
                Block aboveBlock = block.getRelative(0, y, 0);
                if (!aboveBlock.getType().isSolid()) {
                    if (aboveBlock.getLocation().getY() <= world.getMaxHeight()) {
                        return aboveBlock.getLocation().add(0.5, 0, 0.5);
                    } else {
                        player.sendMessage(ChatColor.RED + "¡La ubicación no es segura! Coordenadas: X=" + location.getBlockX() + ", Y=" + location.getBlockY() + ", Z=" + location.getBlockZ());
                        return null;
                    }
                }
            }
        } else {

            for (int y = 1; y <= 3; y++) {
                Block belowBlock = block.getRelative(0, -y, 0);
                if (belowBlock.getType().isSolid()) {
                    return belowBlock.getLocation().add(0.5, 0, 0.5);
                }
            }
        }

        player.sendMessage(ChatColor.RED + "¡No se pudo encontrar una ubicación segura cerca de la casa!");
        return null;
    }



    private void cancelTeleport(Player player) {
        if (teleportTask != null) {
            teleportTask.cancel();
        }
        player.sendMessage(plugin.getMessage("gui.move-cancel"));
        isTeleporting = false;
    }

    // Método para realizar el teletransporte
    private void teleport(Player player, Location targetLocation) {
        player.teleport(targetLocation);
        player.sendMessage(plugin.getMessage("gui.teleported").replace("%name%", "§x§F§B§D§5§9§3" + name));
        isTeleporting = false;
    }

    // Método para iniciar el tiempo de espera antes del teletransporte
    private void startTeleportCooldown(Player player) {
        isTeleporting = true;
        player.sendMessage(plugin.getMessage("gui.teleporting").replace("%name%", "§x§F§B§D§5§9§3" + name));

        Location targetLocation = findSafeLocation(location, player);

        teleportTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isTeleporting) {
                    return; // Si ya se canceló la acción, no hacer nada
                }

                teleport(player, targetLocation);
            }
        }.runTaskLater(plugin, 60L); // 60 ticks = 3 segundos
    }
}
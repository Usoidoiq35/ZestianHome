package p.zestianhome.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import p.zestianhome.gui.HomeMenus;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        if (item.getType() == Material.COMPASS && item.hasItemMeta() &&
                item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().getDisplayName().contains("Home Menu")) {
            HomeMenus.openHomes(event.getPlayer());
        }
    }
}

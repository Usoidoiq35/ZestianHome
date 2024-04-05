package p.zestianhome.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import p.zestianhome.ZestianHome;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class GetHomesItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ZestianHome.getInstance().getMessage("commands.player-only"));
            return true;
        }

        ItemBuilder item = new ItemBuilder(Material.COMPASS)
                .setDisplayName("Home Menu");

        player.getInventory().addItem(item.get());
        player.sendMessage(ZestianHome.getInstance().getMessage("commands.gethomesitem-success"));

        return false;
    }
}

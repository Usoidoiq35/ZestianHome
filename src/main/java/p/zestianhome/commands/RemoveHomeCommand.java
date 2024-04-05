package p.zestianhome.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import p.zestianhome.ZestianHome;

import java.sql.SQLException;

public class RemoveHomeCommand implements CommandExecutor {
    private final ZestianHome plugin;

    public RemoveHomeCommand(ZestianHome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessage("commands.player-only"));
            return true;
        }

        // Check if the player has permission to remove home
        if (!player.hasPermission("zestian.home.remove")) {
            player.sendMessage(plugin.getMessage("commands.removehome-no-permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(plugin.getMessage("commands.removehome-usage"));
            return true;
        }

        String homeName = args[0];

        try {
            plugin.getDatabase().removeHome(player, homeName);
            player.sendMessage(plugin.getMessage("commands.removehome-success").replace("%name%", homeName));
        } catch (SQLException e) {
            player.sendMessage(plugin.getMessage("commands.removehome-error"));
        }

        return true;
    }
}


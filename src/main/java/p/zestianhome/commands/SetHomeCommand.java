package p.zestianhome.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import p.zestianhome.ZestianHome;

import java.sql.SQLException;

public class SetHomeCommand implements CommandExecutor {
    private final ZestianHome plugin;

    public SetHomeCommand(ZestianHome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessage("commands.player-only"));
            return true;
        }

        // Check if the player has permission to set home
        if (!player.hasPermission("zestian.home.1") && !player.hasPermission("zestian.home.*")) {
            player.sendMessage(plugin.getMessage("commands.sethome-no-permission"));
            return true;
        }

        int maxHomes = 1; // Default maximum number of homes
        String permission = null;

        // Check if the player has a specific number of homes permission
        for (String perm : player.getEffectivePermissions().stream()
                .map(p -> p.getPermission().toLowerCase())
                .filter(p -> p.startsWith("zestian.home.") && p.length() > "zestian.home.".length())
                .toArray(String[]::new)) {
            String numberStr = perm.substring("zestian.home.".length());
            try {
                int number = Integer.parseInt(numberStr);
                maxHomes = Math.max(maxHomes, number);
            } catch (NumberFormatException ignored) {
            }
        }

        // Check if the player exceeds the maximum number of homes
        try {
            if (plugin.getDatabase().getHomes(player).size() >= maxHomes) {
                player.sendMessage(plugin.getMessage("commands.sethome-max-homes").replace("%max%", String.valueOf(maxHomes)));
                return true;
            }
        } catch (SQLException e) {
            player.sendMessage(plugin.getMessage("commands.sethome-error"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(plugin.getMessage("commands.sethome-usage"));
            return true;
        }

        try {
            plugin.getDatabase().addHome(player, args[0], player.getLocation());
            player.sendMessage(plugin.getMessage("commands.sethome-success").replace("%name%", args[0]));
        } catch (SQLException e) {
            player.sendMessage(plugin.getMessage("commands.sethome-error"));
        }

        return true;
    }
}

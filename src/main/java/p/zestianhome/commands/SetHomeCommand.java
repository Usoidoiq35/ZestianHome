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
        if (!hasSetHomePermission(player)) {
            player.sendMessage(plugin.getMessage("commands.sethome-no-permission"));
            return true;
        }

        int maxHomes = getMaxHomes(player);

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

    // Método para verificar manualmente el permiso de establecer un home en un número específico
    private boolean hasSetHomePermission(Player player) {
        for (int i = 1; i <= 20; i++) {
            if (player.hasPermission("zestian.home." + i)) {
                return true;
            }
        }
        return player.hasPermission("zestian.home.*");
    }

    // Método para obtener el máximo número de homes que un jugador puede tener
    private int getMaxHomes(Player player) {
        int maxHomes = 1; // Default maximum number of homes
        for (int i = 1; i <= 20; i++) {
            if (player.hasPermission("zestian.home." + i)) {
                maxHomes = i;
            }
        }
        return maxHomes;
    }
}

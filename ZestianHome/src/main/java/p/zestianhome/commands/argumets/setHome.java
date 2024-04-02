package p.zestianhome.commands.argumets;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import p.zestianhome.ZestianHome;
import p.zestianhome.commands.SubCommand;
import p.zestianhome.utils.Manager;

import java.util.Objects;

public class setHome implements SubCommand {

    private final ZestianHome plugin;
    private final Manager manager;

    public setHome(ZestianHome plugin, Manager manager){
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /home setHome <homeName>");
            return;
        }

        String homeName = args[1];
        int maxHomes = plugin.getConfig().getInt("settings.maxHomes");

        if (manager.getHomeCount(player) >= maxHomes) {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.maxHomesReached")));
            player.sendMessage(errorMessage);
            return;
        }

        if (!Manager.homeExists(player, homeName)) {
            Manager.setHome(player, homeName);
            String successMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.success.homeSet")).replace("%home%", homeName));
            player.sendMessage(successMessage);
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeAlreadyExists")).replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }
    }
}
package p.zestianhome.commands.argumets;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import p.zestianhome.ZestianHome;
import p.zestianhome.commands.SubCommand;
import p.zestianhome.utils.Manager;

import java.util.Objects;

public class delHome implements SubCommand {

    private final ZestianHome plugin;

    public delHome(ZestianHome plugin){
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /home delHome <homeName>");
            return;
        }

        String homeName = args[1];
        if (Manager.homeExists(player, homeName)) {
            Manager.deleteHome(player, homeName);
            String successMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.success.homeDeleted")).replace("%home%", homeName));
            player.sendMessage(successMessage);
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeNotFound")).replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }
    }
}

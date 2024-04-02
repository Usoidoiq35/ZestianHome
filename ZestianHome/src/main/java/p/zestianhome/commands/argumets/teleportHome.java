package p.zestianhome.commands.argumets;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import p.zestianhome.ZestianHome;
import p.zestianhome.commands.SubCommand;
import p.zestianhome.utils.CooldownManager;
import p.zestianhome.utils.Manager;

import java.util.Objects;

public class teleportHome implements SubCommand {

    private final ZestianHome plugin;
    private final CooldownManager cooldownManager;

    public teleportHome(ZestianHome plugin){
        this.plugin = plugin;
        this.cooldownManager = new CooldownManager(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args){

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /home home <homeName>");
            return;
        }

        String homeName = args[1];
        if (cooldownManager.hasCooldown(player)) {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.coolDownWait")));
            player.sendMessage(errorMessage);
            return;
        }

        if (Manager.homeExists(player, homeName)) {
            int cooldownSeconds = plugin.getConfig().getInt("settings.cooldownSeconds");
            cooldownManager.startCooldown(player, cooldownSeconds, () -> {cooldownManager.removeCooldown(player);
                Manager.teleportToHome(player, homeName);
            });
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeNotFound")).replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }

    }
}

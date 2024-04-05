package p.zestianhome.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import p.zestianhome.ZestianHome;
import p.zestianhome.gui.HomeMenus;

public class HomesCommand implements CommandExecutor {
    private final ZestianHome plugin;
    private final String PERMISSION = "zestian.home.use";

    public HomesCommand(ZestianHome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getConfig().getString("commands.player-only"));
            return true;
        }
        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(ZestianHome.getInstance().getMessage("commands.not-perms"));
            return true;
        }

        HomeMenus.openHomes(player);

        return true;
    }
}
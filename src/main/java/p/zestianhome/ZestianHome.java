package p.zestianhome;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import p.zestianhome.commands.GetHomesItemCommand;
import p.zestianhome.commands.HomesCommand;
import p.zestianhome.commands.RemoveHomeCommand;
import p.zestianhome.commands.SetHomeCommand;
import p.zestianhome.listeners.PlayerListener;
import p.zestianhome.managers.CooldownManager;
import p.zestianhome.managers.DatabaseManager;

public final class ZestianHome extends JavaPlugin {
    private static ZestianHome instance;

    private DatabaseManager database;


    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        getCommand("homes").setExecutor(new HomesCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("delhome").setExecutor(new RemoveHomeCommand(this));
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);


        try {
            database = new DatabaseManager(this.getConfig().getString("database"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getMessage(String path) {
        String prefix = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.prefix", "§x§F§B§2§2§2§2H§x§F§C§4§5§2§Ao§x§F§D§6§8§3§2m§x§F§E§8§B§3§Ae§x§F§F§A§E§4§2s §7» ")); // Obtener el prefijo de la configuración
        String message = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages." + path, "&cMessage not found: " + path)); // Obtener el mensaje

        return prefix + " " + message;
    }



    public DatabaseManager getDatabase() {
        return database;
    }

    public static ZestianHome getInstance() {
        return instance;
    }

}


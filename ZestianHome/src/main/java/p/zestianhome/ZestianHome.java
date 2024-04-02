package p.zestianhome;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;
import p.zestianhome.commands.HomeCommand;
import p.zestianhome.inventory.HomeInventory;
import p.zestianhome.utils.Manager;

import java.io.File;
import java.util.Objects;

public final class ZestianHome extends JavaPlugin {

    private FileConfiguration homesConfig;
    private File homesFile;
    private Manager manager;

    @Override
    public void onEnable() {

        // Initialize homes file and configuration
        File homesFile = new File(getDataFolder(), "homes.yml");
        if (!homesFile.exists()) {
            homesFile.getParentFile().mkdirs();
            saveResource("homes.yml", false);
        }

        FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);

        Manager manager = new Manager(this, homesConfig, homesFile);

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand(this, manager));
        getServer().getPluginManager().registerEvents(new HomeInventory(this, manager), this);

        saveConfig();
    }

    @Override
    public void onDisable() {
        Manager.saveHomesConfig();
    }
}

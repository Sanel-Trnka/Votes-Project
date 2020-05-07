package de.discordbot;


import de.discordbot.manager.ConfigurationManager;
import de.discordbot.manager.MySqlManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Votes extends JavaPlugin {

    private static ConfigurationManager configManager;
    private static String prefix = "[Votes] ";
    private MySqlManager mySql;

    public static ConfigurationManager getConfigManager(){
        return configManager;
    }

    public static String getPrefix(){
        return prefix;
    }

    public void onEnable() {

        setupConfigurations();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + "Das Votes-Plugin wurde aktiviert!");
        mySql = new MySqlManager();

    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + getPrefix() + "Das Votes-Plugin wurde deaktiviert!");
    }

    // Methode um die Configurationen zu laden
    private void setupConfigurations(){
        configManager = new ConfigurationManager();
        configManager.setup();
    }

}

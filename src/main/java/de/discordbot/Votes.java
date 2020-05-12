package de.discordbot;


//import de.discordbot.command.Daily;
import de.discordbot.command.Daily;
import de.discordbot.listener.onInventoryClickListener;
import de.discordbot.listener.onJoinListener;
import de.discordbot.listener.onVillagerClickListener;
import de.discordbot.manager.ConfigurationManager;
import de.discordbot.manager.MySqlManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Votes extends JavaPlugin {

    // Variablen erstellen
    private static ConfigurationManager configManager;
    private static String prefix = "[Votes] ";
    private static MySqlManager mySql;

    // Getter für das ConfigurationManager-Objekt
    public static ConfigurationManager getConfigManager(){
        return configManager;
    }

    public static MySqlManager getMySqlManager(){
        return mySql;
    }

    // Getter für den Prefix
    public static String getPrefix(){
        return prefix;
    }

    // Startmethode
    public void onEnable() {

        setupConfigurations();
        mySql = new MySqlManager();
        getCommand("daily").setExecutor(new Daily());
        getServer().getPluginManager().registerEvents(new onJoinListener(), this);
        getServer().getPluginManager().registerEvents(new onInventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new onVillagerClickListener(), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + "Das Votes-Plugin wurde aktiviert!");

    }

    // Stoppmethode
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + getPrefix() + "Das Votes-Plugin wurde deaktiviert!");
        try {
            mySql.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // Methode um die Configurationen zu laden
    private void setupConfigurations(){
        configManager = new ConfigurationManager();
        configManager.setup();
    }

}

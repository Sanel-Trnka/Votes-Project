package de.discordbot.manager;

import de.discordbot.Votes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    private Votes plugin = Votes.getPlugin(Votes.class);
    // Erstellen der Files und der FileConfigs
    public FileConfiguration mySqlCfg;
    public File mySqlFile;

    public FileConfiguration langCfg;
    public File langFile;

    public FileConfiguration configCfg;
    public File configFile;

    // Config-Files werden hier deklariert und Startklar gemacht
    public void setup(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        mySqlFile = new File(plugin.getDataFolder(), "mySql.yml");
        langFile = new File(plugin.getDataFolder(), "lang.yml");
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if(!mySqlFile.exists()){
            try{
                mySqlFile.createNewFile();
            }catch(IOException e){
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + Votes.getPrefix() + "Die MySql-Config konnte nicht geladen werden!");
            }

        }

        if(!langFile.exists()){
            try{
                langFile.createNewFile();
            }catch(IOException e){
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + Votes.getPrefix() + "Die Language-Config konnte nicht geladen werden!");
            }

        }

        if(!configFile.exists()){
            try{
                configFile.createNewFile();
            }catch(IOException e){
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(ChatColor.RED + Votes.getPrefix() + "Die Config konnte nicht geladen werden!");
            }

        }

        mySqlCfg = YamlConfiguration.loadConfiguration(mySqlFile);
        langCfg = YamlConfiguration.loadConfiguration(langFile);
        configCfg = YamlConfiguration.loadConfiguration(configFile);
        Bukkit.getServer().getConsoleSender()
                .sendMessage(ChatColor.GREEN + Votes.getPrefix() + "Die Configurationen wurden erfolgreich geladen!");

        loadDefaultMySql();
        loadDefaultLang();
        loadDefaultConfig();

    }

    private void loadDefaultConfig() {

        configCfg.addDefault("daily.basic-reward", 100);
        configCfg.addDefault("daily.reward-command", "/pay [NAME] [MONEY]");

        langCfg.addDefault("daily.inventory-title", "Rewards");

        configCfg.addDefault("daily.item-left.material", "MINECART");
        configCfg.addDefault("daily.item-left.name", "§6Stats");
        configCfg.addDefault("daily.item-left.your-daily", "§aDeine Daily: §e");
        configCfg.addDefault("daily.item-left.your-dailystreak", "§5Dein Dailystreak: §e");

        configCfg.addDefault("daily.item-middle.name", "§6Daily Reward");
        configCfg.addDefault("daily.item-middle.daily-is-available", "§aDu kannst deine tägliche Belohnung §cjetzt§a abholen!");
        configCfg.addDefault("daily.item-middle.daily-is-available-in", "§aDu kannst deine tägliche Belohnung abholen in: §6");
        configCfg.addDefault("daily.item-middle.your-reward", "§9Deine Belohnung: §c");

        configCfg.addDefault("daily.item-right.material", "SIGN");
        configCfg.addDefault("daily.item-right.name", "§6Best Player");
        configCfg.addDefault("daily.item-right.best-daily-1", "§a1. ");
        configCfg.addDefault("daily.item-right.best-daily-2", "§a2. ");
        configCfg.addDefault("daily.item-right.best-daily-3", "§a3. ");

        configCfg.options().copyDefaults(true);
        try{
            configCfg.save(configFile);
        }catch(IOException e){
            Bukkit.getServer().getConsoleSender()
                    .sendMessage(ChatColor.RED + Votes.getPrefix() + "Die Standartwerte für die Language-Configuration wurde nicht geladen!");
        }
    }

    // Lädt die Standartwerte
    private void loadDefaultMySql(){
        mySqlCfg.addDefault("host", "127.0.0.1");
        mySqlCfg.addDefault("port", "3306");
        mySqlCfg.addDefault("username", "root");
        mySqlCfg.addDefault("password", "");
        mySqlCfg.addDefault("database", "exampleDatabase");
        mySqlCfg.options().copyDefaults(true);

        try{
            mySqlCfg.save(mySqlFile);
        }catch(IOException e){
            Bukkit.getServer().getConsoleSender()
                    .sendMessage(ChatColor.RED + Votes.getPrefix() + "Die Standartwerte für die MySql-Configuration wurde nicht geladen!");
        }
    }

    // Lädt die Standartwerte
    private void loadDefaultLang(){



        langCfg.options().copyDefaults(true);
        try{
            langCfg.save(langFile);
        }catch(IOException e){
            Bukkit.getServer().getConsoleSender()
                    .sendMessage(ChatColor.RED + Votes.getPrefix() + "Die Standartwerte für die Language-Configuration wurde nicht geladen!");
        }
    }

    // Gibt einen Wert der Configurationen zurück
    public Object getConfigurationEntry(String cfgType, String path){
        if(cfgType.equalsIgnoreCase("mySql")){
            return mySqlCfg.get(path);
        }else if(cfgType.equalsIgnoreCase("lang")){
            return langCfg.get(path);
        }else if(cfgType.equalsIgnoreCase("config")){
            return configCfg.get(path);
        }
        else{
            return null;
        }
    }

}

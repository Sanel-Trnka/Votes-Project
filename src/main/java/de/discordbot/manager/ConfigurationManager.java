package de.discordbot.manager;

import de.discordbot.Votes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigurationManager {

    private Votes plugin = Votes.getPlugin(Votes.class);
    // Erstellen der Files und der FileConfigs
    public FileConfiguration mySqlCfg;
    public File mySqlFile;

    public FileConfiguration langCfg;
    public File langFile;

    // Config-Files werden hier deklariert und Startklar gemacht
    public void setup(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        mySqlFile = new File(plugin.getDataFolder(), "mySql.yml");
        langFile = new File(plugin.getDataFolder(), "lang.yml");

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

        mySqlCfg = YamlConfiguration.loadConfiguration(mySqlFile);
        langCfg = YamlConfiguration.loadConfiguration(langFile);
        Bukkit.getServer().getConsoleSender()
                .sendMessage(ChatColor.GREEN + Votes.getPrefix() + "Die Configurationen wurden erfolgreich geladen!");

        loadDefaultMySql();
        loadDefaultLang();

    }

    // Lädt die Standartwerte
    private void loadDefaultMySql(){
        HashMap<String, Integer> rewards = new HashMap<String, Integer>();
        rewards.put("daily", 5);
        rewards.put("week", 50);
        rewards.put("month", 500);
        rewards.put("six-month", 48932498);
        rewards.put("year", 48932498);
        mySqlCfg.addDefault("host", "127.0.0.1");
        mySqlCfg.addDefault("port", "3306");
        mySqlCfg.addDefault("username", "root");
        mySqlCfg.addDefault("password", "");
        mySqlCfg.addDefault("database", "exampleDatabase");
        mySqlCfg.addDefault("hashmap", rewards);
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
        langCfg.addDefault("rewardsInventory.title", "Rewards");
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
        }else{
            return null;
        }
    }

}

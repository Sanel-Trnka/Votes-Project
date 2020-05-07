package de.discordbot.manager;

import de.discordbot.Votes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlManager {

    //Benötigte Werte zum Verbinden auf die MySql-DB
    static Connection con = null;
    String host = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "host");
    String port = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "port");
    String database = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "database");
    String username = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "username");
    String password = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "password");
    String table = "accounts";

    //Konstruktor um Verbindung aufzubauen
    public MySqlManager(){
        if(database != "exampleDatabase") {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Votes.getPrefix() + "Die Verbindung zur Datenbank ist hergestellt!");

            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Votes.getPrefix() + "Die Verbindung zur Datenbank ist fehlgeschlagen!");
                Bukkit.getServer().getPluginManager().disablePlugin(Votes.getPlugin(Votes.class));
            }
        }else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Votes.getPrefix() + "Du musst in der mysql.yml die Datenbank eintragen!");
            Bukkit.getServer().getPluginManager().disablePlugin(Votes.getPlugin(Votes.class));
        }
    }



}

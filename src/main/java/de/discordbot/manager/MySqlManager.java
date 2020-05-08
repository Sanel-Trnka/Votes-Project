package de.discordbot.manager;

import de.discordbot.Votes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;

public class MySqlManager {

    //Benötigte Werte zum Verbinden auf die MySql-DB
    private static Connection con = null;
    private String host = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "host");
    private String port = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "port");
    private String database = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "database");
    private String username = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "username");
    private String password = (String) Votes.getConfigManager().getConfigurationEntry("mySql", "password");
    private String table = "accounts";

    //Konstruktor um Verbindung aufzubauen
    public MySqlManager(){
        if(database != "exampleDatabase") {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Votes.getPrefix() + "Die Verbindung zur Datenbank ist hergestellt!");
                setupTable();
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

    // Erstellt eine Tabelle falls nicht schon vorhanden
    private void setupTable(){
        try{
        Statement stmt = con.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS `" + database + "`.`" + table + "` ( `UUID` VARCHAR(36) NOT NULL , `LASTDAILY` DATE NOT NULL , `FIRSTJOIN` DATE NOT NULL );";
        stmt.execute(query);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Votes.getPrefix() + "Die Tabelle wurde erfolgreich aufgesetzt!");
        }catch (SQLException e){
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Votes.getPrefix() + "Das Erstellen der ACCOUNTS-Tabelle ist fehlgeschlagen!");
            Bukkit.getServer().getPluginManager().disablePlugin(Votes.getPlugin(Votes.class));
        }
    }

    // Erstellt mit dem String query ein Statement und schickt diese ab
    public void createStatement(String query){
        try{
        Statement stmt = con.createStatement();
        stmt.executeQuery(query);
        }catch (SQLException e){
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Votes.getPrefix() + "Ein Statement konnte nicht ausgeführt werden!");
            Bukkit.getServer().getPluginManager().disablePlugin(Votes.getPlugin(Votes.class));
        }
    }

    // Erstellt mit dem String query und gibt einen ResultSet zurück
    public ResultSet getResult(String query){
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        }catch (SQLException e){
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Votes.getPrefix() + "Ein ResultSet konnte nicht ausgeführt werden!");
            Bukkit.getServer().getPluginManager().disablePlugin(Votes.getPlugin(Votes.class));
            return null;
        }
    }

}

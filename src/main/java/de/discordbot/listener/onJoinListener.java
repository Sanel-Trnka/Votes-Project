package de.discordbot.listener;

import de.discordbot.Votes;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {

            if(!Votes.getMySqlManager().getResult("SELECT * FROM `accounts` WHERE UUID = '" + e.getPlayer().getUniqueId().toString() + "';").next()){

                Date d = new Date();
                SimpleDateFormat sDF = new SimpleDateFormat("dd/MM/yyyy");

                Votes.getMySqlManager().createStatement("INSERT INTO `accounts`(`UUID`, `FIRSTJOIN`, `Votes`, `Votestreak`, `Daily`, `Dailystreak`) VALUES ( '" + e.getPlayer().getUniqueId().toString() + "', '" + sDF.format(d) +"', 0, 0, 0, 0); ");

            }

    }

}

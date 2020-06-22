package de.discordbot.listener;

import de.discordbot.Votes;
import de.discordbot.manager.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class onInventoryClickListener implements Listener {

    //Initialisiere Variablen
    private String nextDaily = null;
    private long diff = 0;

    //Listener
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws ParseException, SQLException {

        Player p = (Player) e.getWhoClicked();

        if(e.getInventory().getName().equals((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.inventory-title"))){

            e.setCancelled(true);

            if(e.getCurrentItem().getData().getItemType() == e.getInventory().getItem(13).getData().getItemType()){

                ResultSet rs = Votes.getMySqlManager().getResult("SELECT * FROM `accounts` WHERE UUID = '" + p.getUniqueId().toString() + "';");

                while(rs.next()){

                    nextDaily = rs.getString(2);

                    if(rs.getString(2) != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        sdf.applyPattern("dd/MM/yyyy HH:mm:ss");
                        String dateNowString = sdf.format(new Date());

                        Date d1 = sdf.parse(rs.getString(2));
                        Date d2 = sdf.parse(dateNowString);

                        diff = d2.getTime() - d1.getTime();

                    }
                }

                if(diff >= 0 || nextDaily == null){

                    ResultSet rs2 = Votes.getMySqlManager().getResult("SELECT * FROM `accounts` WHERE UUID = '" + p.getUniqueId().toString() + "';");

                    while(rs2.next()){

                        if(diff <= 86400000 || nextDaily == null){
                            Votes.getMySqlManager().createStatement("UPDATE `accounts` SET `Daily` = " + (rs2.getInt(6) + 1) + ", `Dailystreak` = " + (rs2.getInt(7) + 1) + ";");
                        }else{
                            Votes.getMySqlManager().createStatement("UPDATE `accounts` SET `Daily` = " + (rs2.getInt(6) + 1) + ", `Dailystreak` = 0;");
                        }

                    }
                    ResultSet rs3 = Votes.getMySqlManager().getResult("SELECT * FROM `accounts` WHERE UUID = '" + p.getUniqueId().toString() + "';");
                    int dailyStreak = 0;

                    try{

                        while(rs3.next()){
                            dailyStreak = rs3.getInt(7);
                        }

                    } catch (SQLException exception){
                        exception.printStackTrace();
                    }

                    String command = (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.reward-command");
                    String[] commandArray = command.split(" ");

                    for (int i = 0; i < commandArray.length; i++) {
                        if (commandArray[i].equals("[NAME]")) {
                            commandArray[i] = p.getName();
                        } else if (commandArray[i].equals("[MONEY]")) {
                            int rewardCoins = (Integer) Votes.getConfigManager().getConfigurationEntry("config", "daily.basic-reward");

                            for(String key : ConfigurationManager.permissionRewards.keySet()){

                                if(p.hasPermission(key.toString())){
                                    Bukkit.getConsoleSender().sendMessage("Ja Moin!");
                                    rewardCoins = ConfigurationManager.permissionRewards.get(key);
                                }

                            }

                            int reward = rewardCoins * dailyStreak;
                            commandArray[i] = Integer.toString(reward);
                        }

                    }

                    String commandExecute = String.join(" ", commandArray);

                    p.closeInventory();
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), commandExecute);

                    SimpleDateFormat sdf = new SimpleDateFormat();
                    sdf.applyPattern("dd/MM/yyyy HH:mm:ss");
                    String dateNowString = sdf.format(new Date());

                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(dateNowString));
                    c.add(Calendar.DATE, 1);
                    String dateNextDaily = sdf.format(c.getTime());
                    Votes.getMySqlManager().createStatement("UPDATE `accounts` SET `NEXTDAILY` = '" + dateNextDaily + "';");

                }
            }
        }
    }
}

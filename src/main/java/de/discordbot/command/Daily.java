package de.discordbot.command;

import de.discordbot.Votes;
import de.discordbot.manager.ConfigurationManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Daily implements CommandExecutor {

    private String nextDaily = null;
    private String firstJoin = null;
    private String votes = null;
    private String voteStreak = null;
    private String daily = null;
    private String dailyStreak = null;

    List<String> mostDaily = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                // Werte aus der DB aufrufen ------------------------------------------------------------------------------------------------------

                ResultSet rs = Votes.getMySqlManager().getResult("SELECT * FROM `accounts` WHERE UUID = '" + p.getUniqueId().toString() + "';");
                ResultSet rs2 = Votes.getMySqlManager().getResult("SELECT UUID, Daily FROM `accounts` ORDER BY Daily DESC LIMIT 3;");

                try {
                    while (rs2.next()) mostDaily.add(rs2.getString(1));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    while (rs.next()) {

                        nextDaily = rs.getString(2);
                        firstJoin = rs.getString(3);
                        votes = rs.getString(4);
                        voteStreak = rs.getString(5);
                        daily = rs.getString(6);
                        dailyStreak = rs.getString(7);

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //-------------------------------------------------------------------------------------------------------------------------------------------

                Inventory inventory = Bukkit.createInventory(null, 9 * 3, (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.inventory-title"));

                // Erstellen der Items
                createLeftItem(inventory);
                try {
                    createMiddleItem(inventory, p);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                createRightItem(inventory);

                p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 40, 2.0D, 3.0D, 2.0D, 5.0D);
                p.openInventory(inventory);

            }

            return false;
        } else if (sender instanceof Player) {
            Player p = (Player) sender;

            if (Votes.getConfigManager().existLobbyConfig()) {


                if (args.length == 1 && args[0].equalsIgnoreCase((String) Votes.getConfigManager().getConfigurationEntry("lobby", "daily.villager.command"))) {


                    if (p.hasPermission("*")) {


                        Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                        v.setCustomName((String) Votes.getConfigManager().getConfigurationEntry("lobby", "daily.villager.custom_name"));
                        p.sendMessage((String) Votes.getConfigManager().getConfigurationEntry("lobby", "villager-created"));


                    } else {
                        p.sendMessage(ChatColor.RED + Votes.getPrefix() + (String) Votes.getConfigManager().getConfigurationEntry("lang", "no-permission"));
                    }

                } else {
                        p.sendMessage(ChatColor.RED + Votes.getPrefix() + (String) Votes.getConfigManager().getConfigurationEntry("lang", "try-other-command"));
                }

            }else {
                p.sendMessage(ChatColor.RED + Votes.getPrefix() + (String) Votes.getConfigManager().getConfigurationEntry("lang", "just-lobby"));
            }
            return false;
        }
        return false;
    }

    public void createLeftItem(Inventory inventory){
        ItemStack itemLeft = new ItemStack(Material.getMaterial((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-left.material")), 1);
        ItemMeta itemMetaLeft = itemLeft.getItemMeta();

        itemMetaLeft.setDisplayName((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-left.name"));
        List<String> lore = Arrays.asList((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-left.your-daily") + daily, (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-left.your-dailystreak") + dailyStreak);
        itemMetaLeft.setLore(lore);
        itemLeft.setItemMeta(itemMetaLeft);
        inventory.setItem(11, itemLeft);
    }

    public void createMiddleItem(Inventory inventory, Player p) throws ParseException, NumberFormatException {
        ItemStack itemMiddle = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

        SkullMeta skullMeta = (SkullMeta) itemMiddle.getItemMeta();
        skullMeta.setOwner(p.getName());
        skullMeta.setDisplayName((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-middle.name"));

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy HH:mm:ss");

        List<String> lore;
        long diff = 0;

        if(nextDaily != null){

            String dateNowString = sdf.format(new Date());

            Date d1 = sdf.parse(nextDaily);
            Date d2 = sdf.parse(dateNowString);

            diff = d2.getTime() - d1.getTime();

        }

        if(nextDaily == null || diff > 0){
            int rewardCoins = (Integer) Votes.getConfigManager().getConfigurationEntry("config", "daily.basic-reward");

            for(String key : ConfigurationManager.permissionRewards.keySet()){
                System.out.println(key);
                if(p.hasPermission(key.toString())){
                    rewardCoins = ConfigurationManager.permissionRewards.get(key);
                }

            }
            lore = Arrays.asList((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-middle.daily-is-available"), (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-middle.your-reward") + (Integer.parseInt(dailyStreak) + 1) * rewardCoins);
        }else{

            String dateNowString = sdf.format(new Date());

            Date d1 = sdf.parse(nextDaily);
            Date d2 = sdf.parse(dateNowString);

            long diff2 = d1.getTime() - d2.getTime();
            long diffSeconds = diff2 / 1000 % 60;
            long diffMinutes = diff2 / (60 * 1000) % 60;
            long diffHours = diff2 / (60 * 60 * 1000) % 24;

            lore = Arrays.asList((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-middle.daily-is-available-in") + diffHours + " Std. " + diffMinutes + " Min. " + diffSeconds + " Sec.");
        }


        skullMeta.setLore(lore);
        itemMiddle.setItemMeta(skullMeta);
        inventory.setItem(13, itemMiddle);
    }

    public void createRightItem(Inventory inventory){

        ItemStack itemRight = new ItemStack(Material.getMaterial((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.material")), 1);
        ItemMeta itemMetaRight = itemRight.getItemMeta();

        itemMetaRight.setDisplayName((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.name"));

        List<String> lore = new ArrayList<String>();

        if(mostDaily.size() == 1) {
            lore = Arrays.asList((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-1") + Bukkit.getOfflinePlayer(UUID.fromString(mostDaily.get(0))).getName(), (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-2") + "-----", (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-3") + "-----");
        }else if(mostDaily.size() == 2){
            lore = Arrays.asList((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-1") + Bukkit.getOfflinePlayer(UUID.fromString(mostDaily.get(0))).getName(), (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-2") + Bukkit.getOfflinePlayer(UUID.fromString(mostDaily.get(1))).getName(), (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-3") + "-----");
        }else{
            lore = Arrays.asList((String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-1") + Bukkit.getOfflinePlayer(UUID.fromString(mostDaily.get(0))).getName(), (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-2") + Bukkit.getOfflinePlayer(UUID.fromString(mostDaily.get(1))).getName(), (String) Votes.getConfigManager().getConfigurationEntry("config", "daily.item-right.best-daily-3") + Bukkit.getOfflinePlayer(UUID.fromString(mostDaily.get(2))).getName());
        }
        mostDaily.clear();
        itemMetaRight.setLore(lore);
        itemRight.setItemMeta(itemMetaRight);
        inventory.setItem(15, itemRight);

    }

}
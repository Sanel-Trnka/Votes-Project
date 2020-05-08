/*package de.discordbot.command;

import de.discordbot.Votes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Daily implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;

            Inventory inventory = Bukkit.createInventory(null, 9*3, (String) Votes.getConfigManager().getConfigurationEntry("lang", "rewardsInventory.title"));
            p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 40, 2.0D, 3.0D, 2.0D, 5.0D);
            p.openInventory(inventory);

        }

        return false;
    }

}
*/
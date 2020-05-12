package de.discordbot.listener;

import de.discordbot.Votes;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class onVillagerClickListener implements Listener {

    @EventHandler
    public void onVillagerClick(PlayerInteractEntityEvent e) {

        Player p = (Player) e.getPlayer();

        if(e.getRightClicked().getType().equals(EntityType.VILLAGER)){
            Villager v = (Villager) e.getRightClicked();

            if(v.getCustomName().equalsIgnoreCase((String) Votes.getConfigManager().getConfigurationEntry("lobby", "daily.villager.custom_name"))){

                e.setCancelled(true);
                Bukkit.getServer().dispatchCommand(p, "daily");

            }

        }
    }

}

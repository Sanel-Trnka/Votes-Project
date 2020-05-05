package de.discordbot;

import net.md_5.bungee.api.plugin.Plugin;

public class Votes extends Plugin {

    public void onEnable() {
        getLogger().info("§aDas Votes-Plugin wurde aktiviert!");
    }

    public void onDisable() {
        getLogger().info("§cDas Votes-Plugin wurde deaktiviert!");
    }

}

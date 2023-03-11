package com.facuu16.betterdrops;

import com.facuu16.betterdrops.command.BetterDropsCommand;
import com.facuu16.betterdrops.event.BlockBreak;
import com.facuu16.betterdrops.manager.DropManager;
import com.facuu16.betterdrops.event.EntityDeath;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getPluginManager;

public class BetterDrops extends JavaPlugin {
    @Override
    public void onEnable() {
        registerConfig();
        registerManagers();
        registerEvents();
        registerCommands();
        getLogger().info("BetterDrops " + getDescription().getVersion() + " started successfully, by: Facuu16.");
    }

    private void registerManagers() {
        DropManager.getInstance(this);
        getLogger().info("Managers loaded.");
    }

    private void registerCommands() {
        getCommand("betterdrops").setExecutor(new BetterDropsCommand(this));
        getLogger().info("Commands loaded.");
    }

    private void registerEvents() {
        getPluginManager().registerEvents(new EntityDeath(this), this);
        getPluginManager().registerEvents(new BlockBreak(this), this);
        getLogger().info("Events loaded.");
    }

    private void registerConfig() {
        File config = new File(getDataFolder(), "config.yml");

        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
            getLogger().info("Config.yml created.");
        }
    }

    public String translate(String value) {
        return ChatColor.translateAlternateColorCodes('&', value);
    }
}

package com.facuu16.betterdrops;

import com.facuu16.betterdrops.command.BetterDropsCommand;
import com.facuu16.betterdrops.listener.BlockBreakListener;
import com.facuu16.betterdrops.manager.DropManager;
import com.facuu16.betterdrops.listener.EntityDeathListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static org.bukkit.Bukkit.getPluginManager;

public class BetterDrops extends JavaPlugin {
    public void onEnable() {
        registerConfig();
        initialize();
        registerEvents();
        registerCommands();
        getLogger().info("BetterDrops " + getDescription().getVersion() + " started successfully, by: Facuu16.");
    }

    private void initialize() {
        DropManager.getInstance(this).reload();
        getLogger().info("Managers loaded.");
    }

    private void registerCommands() {
        getCommand("betterdrops").setExecutor(new BetterDropsCommand(this));
        getLogger().info("Commands loaded.");
    }

    private void registerEvents() {
        getPluginManager().registerEvents(new EntityDeathListener(this), this);
        getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getLogger().info("Events loaded.");
    }

    private void registerConfig() {
        File config = new File(this.getDataFolder(), "config.yml");

        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveDefaultConfig();
            getLogger().info("Config.yml created.");
        }
    }

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}

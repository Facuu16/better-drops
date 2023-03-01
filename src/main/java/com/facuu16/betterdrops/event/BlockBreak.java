package com.facuu16.betterdrops.event;

import com.facuu16.betterdrops.BetterDrops;
import com.facuu16.betterdrops.drop.*;
import com.facuu16.betterdrops.manager.DropManager;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BlockBreak implements Listener {
    private final BetterDrops plugin;

    public BlockBreak(BetterDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        DropManager manager = DropManager.getInstance(plugin);

        if (event.isCancelled())
            return;

        for (Map.Entry<String, Drop> entry : manager.getDrops(DropType.BLOCK).entrySet()) {
            Block block = event.getBlock();
            DropBlock dropBlock = (DropBlock) entry.getValue();

            if (dropBlock.getBlock() != block.getType())
                continue;

            Location location = block.getLocation();

            if (!dropBlock.getWorlds().contains(location.getWorld().getName()))
                continue;

            boolean removed = false;

            for (Droppable droppable : dropBlock.getItems()) {
                if (droppable.getProbability() < Math.random() * 100)
                    continue;

                if (!dropBlock.isKeep() && !removed) {
                    removed = true;
                    event.setCancelled(true);
                    block.setType(Material.AIR);
                }

                NBTCompound itemNBT = (NBTCompound) droppable.getNBT();
                ItemStack item = NBTItem.convertNBTtoItem(itemNBT);

                location.getWorld().dropItemNaturally(location, item);

                for (String command : droppable.getCommands()) {
                    Player player = event.getPlayer();

                    if (player != null)
                        command = command.replace("<player>", player.getName());

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
        }
    }
}

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

public class BlockBreak implements Listener {
    private final BetterDrops plugin;

    public BlockBreak(BetterDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        Block block = event.getBlock();
        Location location = block.getLocation();

        DropManager.getInstance(plugin).getDrops(DropType.BLOCK).values().stream()
                .map(drop -> (DropBlock) drop)
                .filter(drop -> drop.getBlock() == block.getType()
                        && drop.getWorlds().contains(location.getWorld().getName()))
                .findFirst()
                .ifPresent(drop -> {
                    boolean removed = false;

                    for (Droppable droppable : drop.getItems()) {
                        if (droppable.getProbability() < Math.random() * 100)
                            continue;

                        if (!drop.isKeep() && !removed) {
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
                });
    }
}

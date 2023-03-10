package com.facuu16.betterdrops.event;

import com.facuu16.betterdrops.BetterDrops;
import com.facuu16.betterdrops.drop.DropEntity;
import com.facuu16.betterdrops.drop.Droppable;
import com.facuu16.betterdrops.drop.DropType;
import com.facuu16.betterdrops.manager.DropManager;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EntityDeath implements Listener {
    private final BetterDrops plugin;

    public EntityDeath(BetterDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        DropManager manager = DropManager.getInstance(plugin);

        Entity entity = event.getEntity();
        String world = entity.getWorld().getName();

        manager.getDrops(DropType.ENTITY).values().stream()
                .map(drop -> (DropEntity) drop)
                .filter(drop -> drop.getEntity() == event.getEntityType()
                        && drop.getWorlds().contains(world))
                .findFirst()
                .ifPresent(drop -> {
                    List<ItemStack> eventDrops = event.getDrops();
                    boolean removed = false;

                    for (Droppable droppable : drop.getItems()) {
                        if (droppable.getProbability() < Math.random() * 100)
                            continue;

                        if (!drop.isKeep() && !removed) {
                            removed = true;
                            eventDrops.clear();
                        }

                        NBTCompound itemNBT = (NBTCompound) droppable.getNBT();
                        ItemStack item = NBTItem.convertNBTtoItem(itemNBT);

                        eventDrops.add(item);

                        for (String command : droppable.getCommands()) {
                            Player player = event.getEntity().getKiller();

                            if (player != null)
                                command = command.replace("<player>", player.getName());

                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                        }
                    }
                });
    }
}

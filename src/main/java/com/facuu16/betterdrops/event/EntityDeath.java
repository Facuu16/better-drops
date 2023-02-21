package com.facuu16.betterdrops.event;

import com.facuu16.betterdrops.BetterDrops;
import com.facuu16.betterdrops.drop.DropEntity;
import com.facuu16.betterdrops.drop.Droppable;
import com.facuu16.betterdrops.drop.DropType;
import com.facuu16.betterdrops.drop.Drop;
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
import java.util.Map;

public class EntityDeath implements Listener {
    private final BetterDrops plugin;

    public EntityDeath(BetterDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        DropManager manager = DropManager.getInstance(plugin);

        for (Map.Entry<String, Drop> entry : manager.getDrops(DropType.ENTITY).entrySet()) {
            Entity entity = event.getEntity();
            DropEntity dropEntity = (DropEntity) entry.getValue();

            if (dropEntity.getEntity() != entity.getType())
                continue;

            if (!dropEntity.getWorlds().contains(entity.getWorld().getName()))
                continue;

            List<ItemStack> eventDrops = event.getDrops();
            boolean removed = false;

            for (Droppable droppable : dropEntity.getItems()) {
                if (droppable.getProbability() < Math.random() * 100)
                    continue;

                if (!dropEntity.getKeep() && !removed) {
                    removed = true;
                    eventDrops.clear();
                }

                NBTCompound itemNBT = (NBTCompound) droppable.getNBT();
                eventDrops.add(NBTItem.convertNBTtoItem(itemNBT));

                for (String command : droppable.getCommands()) {
                    Player player = event.getEntity().getKiller();

                    if (player != null)
                        command = command.replaceAll("<player>", player.getName());

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
        }
    }
}

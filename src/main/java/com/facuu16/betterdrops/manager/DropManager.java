package com.facuu16.betterdrops.manager;

import com.facuu16.betterdrops.BetterDrops;
import com.facuu16.betterdrops.drop.*;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NbtApiException;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DropManager {
    private final Map<DropType, Map<String, Drop>> DROPS = new EnumMap<>(DropType.class);

    private final BetterDrops plugin;

    private static DropManager instance;

    private DropManager(BetterDrops plugin) {
        this.plugin = plugin;

        for (DropType type : DropType.values())
            DROPS.put(type, new HashMap<>());
    }

    public static DropManager getInstance(BetterDrops plugin) {
        if (instance == null) {
            instance = new DropManager(plugin);

            try {
                instance.reload();
            } catch (Exception exception) {
                plugin.getLogger().log(Level.SEVERE, "An error occurred while reloading the configuration", exception);
            }
        }

        return instance;
    }

    public void updateDrop(DropType type, Drop drop) {
        DROPS.get(type).put(drop.getId(), drop);
    }

    public void removeDrop(DropType type, String id) {
        DROPS.get(type).remove(id);
    }

    public Drop getDrop(DropType type, String id) {
        return DROPS.get(type).get(id);
    }

    public Map<String, Drop> getDrops(DropType type) {
        return DROPS.get(type);
    }

    public boolean containsDrop(DropType type, String id) {
        return DROPS.get(type).containsKey(id);
    }

    public void removeDrops(DropType type) {
        DROPS.get(type).clear();
    }

    public void reload() throws NullPointerException, NbtApiException, IllegalArgumentException {
        final ConfigurationSection items = plugin.getConfig()
                .getConfigurationSection("items");

        final List<Droppable> droppables = items.getKeys(false)
                .stream()
                .map(items::getConfigurationSection)
                .map(item ->
                        new Droppable(
                                item.getName(),
                                NBT.parseNBT(item.getString("NBT")),
                                item.getDouble("probability"),
                                item.getStringList("commands")
                        ))
                .collect(Collectors.toList());

        process(DropType.BLOCK, droppables);
        process(DropType.ENTITY, droppables);
    }

    private void process(DropType dropType, List<Droppable> droppables) {
        final ConfigurationSection drops = plugin.getConfig()
                .getConfigurationSection("drops-" + dropType.toString().toLowerCase());

        drops.getKeys(false).forEach(dropId -> {
            final ConfigurationSection drop = drops.getConfigurationSection(dropId);
            final boolean keep = drop.getBoolean("keep-original-drops");
            final List<String> worlds = drop.getStringList("worlds");

            final List<Droppable> selected = new ArrayList<>();
            for (String id : drop.getStringList("items")) {
                droppables.stream()
                        .filter(item -> item.getId().equals(id))
                        .findFirst().ifPresent(selected::add);
            }

            switch (dropType) {
                case ENTITY:
                    updateDrop(dropType, new DropEntity(dropId, keep, worlds, selected, EntityType.valueOf(drop.getString("entity"))));
                    break;

                case BLOCK:
                    updateDrop(dropType, new DropBlock(dropId, keep, worlds, selected, Material.valueOf(drop.getString("block"))));
                    break;
            }
        });
    }
}

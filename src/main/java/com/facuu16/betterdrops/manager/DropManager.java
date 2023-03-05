package com.facuu16.betterdrops.manager;

import com.facuu16.betterdrops.BetterDrops;
import com.facuu16.betterdrops.drop.*;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NbtApiException;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.*;

public class DropManager {
    private final Map<DropType, Map<String, Drop>> dropMap = new EnumMap<>(DropType.class);

    private final BetterDrops plugin;

    private static DropManager instance;

    private DropManager(BetterDrops plugin) {
        this.plugin = plugin;

        for (DropType type : DropType.values())
            dropMap.put(type, new HashMap<>());
    }

    public static DropManager getInstance(BetterDrops plugin) {
        if (instance == null) {
            instance = new DropManager(plugin);
            instance.reload();
        }

        return instance;
    }

    public void addDrop(DropType type, Drop drop) {
        dropMap.get(type).put(drop.getId(), drop);
    }

    public void removeDrop(DropType type, String id) {
        dropMap.get(type).remove(id);
    }

    public void clearDrops(DropType type) {
        dropMap.get(type).clear();
    }

    public Drop getDrop(DropType type, String id) {
         return dropMap.get(type).get(id);
    }

    public Map<String, Drop> getDrops(DropType type) {
        return dropMap.get(type);
    }

    public boolean contains(DropType type, String id) {
        return dropMap.get(type).containsKey(id);
    }

    public void reload() throws NbtApiException, NullPointerException, IllegalArgumentException {
        final FileConfiguration config = plugin.getConfig();

        if (config == null) {
            plugin.getLogger().severe("The config.yml file was not found");
            return;
        }

        final ConfigurationSection itemsSection = config.getConfigurationSection("items");
        final Map<String, Droppable> items = new HashMap<>();

        if (itemsSection != null) {
            for (String itemId : itemsSection.getKeys(false)) {
                final ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemId);

                try {
                    items.put(itemId, new Droppable(
                            NBT.parseNBT(itemSection.getString("NBT")),
                            itemSection.getDouble("probability"),
                            itemSection.getStringList("commands")
                    ));
                } catch (NbtApiException | NullPointerException exception) {
                    plugin.getLogger().severe("There was an error processing the item: " + itemId);
                }
            }
        }

        final ConfigurationSection blocksSection = config.getConfigurationSection("drops-block");
        final ConfigurationSection entitiesSection = config.getConfigurationSection("drops-entity");

        processDrops(DropType.BLOCK, blocksSection, items);
        processDrops(DropType.ENTITY, entitiesSection, items);
    }

    private void processDrops(DropType dropType, ConfigurationSection drops, Map<String, Droppable> items) {
        if (drops == null)
            return;

        for (String dropId : drops.getKeys(false)) {
            final ConfigurationSection drop = drops.getConfigurationSection(dropId);
            final List<Droppable> droppables = new ArrayList<>();

            clearDrops(dropType); // temporary

            try {
                for (String id : drop.getStringList("items")) {
                    if (items.containsKey(id))
                        droppables.add(items.get(id));
                }

                final boolean keepOriginalDrops = drop.getBoolean("keep-original-drops");
                final List<String> worlds = drop.getStringList("worlds");

                if (dropType == DropType.BLOCK) {
                    Material block = Material.valueOf(drop.getString("block"));
                    addDrop(DropType.BLOCK, new DropBlock(dropId, keepOriginalDrops, worlds, droppables, block));
                }

                else if (dropType == DropType.ENTITY) {
                    EntityType entity = EntityType.valueOf(drop.getString("entity"));
                    addDrop(DropType.ENTITY, new DropEntity(dropId, keepOriginalDrops, worlds, droppables, entity));
                }
            } catch (NbtApiException | NullPointerException | IllegalArgumentException exception) {
                plugin.getLogger().severe("There was an error loading the "
                        + dropType.name().toLowerCase() + ": " + dropId);
            }
        }
    }
}

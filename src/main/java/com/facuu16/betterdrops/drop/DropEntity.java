package com.facuu16.betterdrops.drop;

import org.bukkit.entity.EntityType;

import java.util.List;

public final class DropEntity extends Drop {
    private final EntityType entity;

    public DropEntity(String id, boolean keepOriginalDrops, List<String> worlds, List<Droppable> items, EntityType entity) {
        super(id, keepOriginalDrops, worlds, items);
        this.entity = entity;
    }

    public EntityType getEntity() {
        return entity;
    }
}

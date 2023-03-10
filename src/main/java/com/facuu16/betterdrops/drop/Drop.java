package com.facuu16.betterdrops.drop;

import java.util.List;

public abstract class Drop {
    private final String id;

    private final boolean keepOriginalDrops;

    private final List<String> worlds;

    private final List<Droppable> items;

    protected Drop(String id, boolean keepOriginalDrops, List<String> worlds, List<Droppable> items) {
        this.id = id;
        this.keepOriginalDrops = keepOriginalDrops;
        this.worlds = worlds;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public boolean isKeep() {
        return keepOriginalDrops;
    }

    public List<String> getWorlds() {
        return worlds;
    }

    public List<Droppable> getItems() {
        return items;
    }
}

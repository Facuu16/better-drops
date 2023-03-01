package com.facuu16.betterdrops.drop;

import java.util.List;

public abstract class Drop {
    private String id;

    private boolean keepOriginalDrops;

    private List<String> worlds;

    private List<Droppable> items;

    protected Drop(String id, boolean keepOriginalDrops, List<String> worlds, List<Droppable> items) {
        this.id = id;
        this.keepOriginalDrops = keepOriginalDrops;
        this.worlds = worlds;
        this.items = items;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKeep(boolean keepOriginalDrops) {
        this.keepOriginalDrops = keepOriginalDrops;
    }

    public void setWorlds(List<String> worlds) {
        this.worlds = worlds;
    }

    public void setItems(List<Droppable> items) {
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

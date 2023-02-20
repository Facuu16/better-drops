package com.facuu16.betterdrops.drop;

import org.bukkit.Material;

import java.util.List;

public class DropBlock extends Drop {
    private Material block;

    public DropBlock(String id, boolean keepOriginalDrops, List<String> worlds, List<Droppable> items, Material block) {
        super(id, keepOriginalDrops, worlds, items);
        this.block = block;
    }

    public void setBlock(Material block) {
        this.block = block;
    }

    public Material getBlock() {
        return block;
    }
}

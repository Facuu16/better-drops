package com.facuu16.betterdrops.drop;

import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import java.util.List;

public final class Droppable {
    private final String id;

    private final ReadWriteNBT NBT;

    private final double probability;

    private final List<String> commands;

    public Droppable(String id, ReadWriteNBT NBT, double probability, List<String> commands) {
        this.id = id;
        this.NBT = NBT;
        this.probability = probability;
        this.commands = commands;
    }

    public String getId() {
        return id;
    }

    public ReadWriteNBT getNBT() {
        return NBT;
    }

    public double getProbability() {
        return probability;
    }

    public List<String> getCommands() {
        return commands;
    }
}

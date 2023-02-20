package com.facuu16.betterdrops.drop;

import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import java.util.List;

public class Droppable {
    private ReadWriteNBT NBT;

    private double probability;

    private List<String> commands;

    public Droppable(ReadWriteNBT NBT, double probability, List<String> commands) {
        this.NBT = NBT;
        this.probability = probability;
        this.commands = commands;
    }

    public void setNBT(ReadWriteNBT NBT) {
        this.NBT = NBT;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
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

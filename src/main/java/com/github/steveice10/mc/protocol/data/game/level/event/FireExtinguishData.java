package com.github.steveice10.mc.protocol.data.game.level.event;

public enum FireExtinguishData implements LevelEventData {
    EXTINGUISH,
    GENERIC_EXTINGUISH;

    private static final FireExtinguishData[] VALUES = values();

    public static FireExtinguishData from(int id) {
        return VALUES[id];
    }
}

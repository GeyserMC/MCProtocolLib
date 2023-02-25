package com.github.steveice10.mc.protocol.data.game.level.event;

public enum ComposterEventData implements LevelEventData {
    FILL,
    FILL_SUCCESS;

    private static final ComposterEventData[] VALUES = values();

    public static ComposterEventData from(int id) {
        return VALUES[id];
    }
}

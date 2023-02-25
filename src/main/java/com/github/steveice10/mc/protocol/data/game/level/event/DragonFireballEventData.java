package com.github.steveice10.mc.protocol.data.game.level.event;

public enum DragonFireballEventData implements LevelEventData {
    NO_SOUND,
    HAS_SOUND;

    private static final DragonFireballEventData[] VALUES = values();

    public static DragonFireballEventData from(int id) {
        return VALUES[id];
    }
}

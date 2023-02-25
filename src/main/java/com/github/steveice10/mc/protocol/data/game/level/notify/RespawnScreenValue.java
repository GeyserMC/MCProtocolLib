package com.github.steveice10.mc.protocol.data.game.level.notify;

public enum RespawnScreenValue implements GameEventValue {
    ENABLE_RESPAWN_SCREEN,
    IMMEDIATE_RESPAWN;

    private static final RespawnScreenValue[] VALUES = values();

    public static RespawnScreenValue from(int id) {
        return VALUES[id];
    }
}

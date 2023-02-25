package com.github.steveice10.mc.protocol.data.game;

public enum BossBarAction {
    ADD,
    REMOVE,
    UPDATE_HEALTH,
    UPDATE_TITLE,
    UPDATE_STYLE,
    UPDATE_FLAGS;

    private static final BossBarAction[] VALUES = values();

    public static BossBarAction from(int id) {
        return VALUES[id];
    }
}

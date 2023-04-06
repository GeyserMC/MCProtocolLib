package com.github.steveice10.mc.protocol.data.game.scoreboard;

public enum TeamAction {
    CREATE,
    REMOVE,
    UPDATE,
    ADD_PLAYER,
    REMOVE_PLAYER;

    private static final TeamAction[] VALUES = values();

    public static TeamAction from(int id) {
        return VALUES[id];
    }
}

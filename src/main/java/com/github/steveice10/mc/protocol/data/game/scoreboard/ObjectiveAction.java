package com.github.steveice10.mc.protocol.data.game.scoreboard;

public enum ObjectiveAction {
    ADD,
    REMOVE,
    UPDATE;

    private static final ObjectiveAction[] VALUES = values();

    public static ObjectiveAction from(int id) {
        return VALUES[id];
    }
}

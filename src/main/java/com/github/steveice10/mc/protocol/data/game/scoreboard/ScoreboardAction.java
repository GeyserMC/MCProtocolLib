package com.github.steveice10.mc.protocol.data.game.scoreboard;

public enum ScoreboardAction {
    ADD_OR_UPDATE,
    REMOVE;

    private static final ScoreboardAction[] VALUES = values();

    public static ScoreboardAction from(int id) {
        return VALUES[id];
    }
}

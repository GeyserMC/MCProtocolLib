package com.github.steveice10.mc.protocol.data.game.scoreboard;

public enum ScoreType {
    INTEGER,
    HEARTS;

    private static final ScoreType[] VALUES = values();

    public static ScoreType from(int id) {
        return VALUES[id];
    }
}

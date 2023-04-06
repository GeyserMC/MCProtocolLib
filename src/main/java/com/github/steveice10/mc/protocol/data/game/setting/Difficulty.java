package com.github.steveice10.mc.protocol.data.game.setting;

public enum Difficulty {
    PEACEFUL,
    EASY,
    NORMAL,
    HARD;

    private static final Difficulty[] VALUES = values();

    public static Difficulty from(int id) {
        return VALUES[id];
    }
}

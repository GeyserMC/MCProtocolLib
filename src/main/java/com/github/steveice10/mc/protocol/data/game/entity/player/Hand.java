package com.github.steveice10.mc.protocol.data.game.entity.player;

public enum Hand {
    MAIN_HAND,
    OFF_HAND;

    private static final Hand[] VALUES = values();

    public static Hand from(int id) {
        return VALUES[id];
    }
}

package com.github.steveice10.mc.protocol.data.game.entity.player;

public enum Animation {
    SWING_ARM,
    DAMAGE,
    LEAVE_BED,
    SWING_OFFHAND,
    CRITICAL_HIT,
    ENCHANTMENT_CRITICAL_HIT;

    private static final Animation[] VALUES = values();

    public static Animation from(int id) {
        return VALUES[id];
    }
}

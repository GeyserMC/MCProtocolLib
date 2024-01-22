package com.github.steveice10.mc.protocol.data.game.entity.metadata;

public enum ArmadilloState {
    IDLE,
    ROLLING,
    SCARED;

    private static final ArmadilloState[] VALUES = values();

    public static ArmadilloState from(int id) {
        return VALUES[id];
    }
}

package com.github.steveice10.mc.protocol.data.game.level.block.value;

public enum PistonValueType implements BlockValueType {
    PUSHING,
    PULLING,
    CANCELLED_MID_PUSH;

    private static final PistonValueType[] VALUES = values();

    public static PistonValueType from(int id) {
        return VALUES[id];
    }
}

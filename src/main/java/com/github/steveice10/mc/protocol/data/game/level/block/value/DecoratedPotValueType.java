package com.github.steveice10.mc.protocol.data.game.level.block.value;

public enum DecoratedPotValueType implements BlockValueType {
    WOBBLE_STYLE;

    private static final DecoratedPotValueType[] VALUES = values();

    public static DecoratedPotValueType from(int id) {
        return VALUES[id];
    }
}

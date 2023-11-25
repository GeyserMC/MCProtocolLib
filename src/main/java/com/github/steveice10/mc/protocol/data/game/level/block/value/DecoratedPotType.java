package com.github.steveice10.mc.protocol.data.game.level.block.value;

public enum DecoratedPotType implements BlockValueType{
    WOBBLE_STYLE;

    private static final DecoratedPotType[] VALUES = values();

    public static DecoratedPotType from(int id) {
        return VALUES[id];
    }
}

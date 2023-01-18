package com.github.steveice10.mc.protocol.data.game.level.block.value;

public enum GenericBlockValueType implements BlockValueType {
    GENERIC_0,
    GENERIC_1;

    private static final GenericBlockValueType[] VALUES = values();

    public static GenericBlockValueType from(int id) {
        return VALUES[id];
    }
}

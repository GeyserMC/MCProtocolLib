package com.github.steveice10.mc.protocol.data.game.level.block.value;

import com.github.steveice10.mc.protocol.data.UnmappedValueException;

public enum BellValueType implements BlockValueType {
    SHAKE_DIRECTION;

    private static final BellValueType[] VALUES = values();

    public static BellValueType from(int id) {
        return VALUES[id];
    }
}

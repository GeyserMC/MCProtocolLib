package com.github.steveice10.mc.protocol.data.game.level.block.value;

public enum ChestValueType implements BlockValueType {
    VIEWING_PLAYER_COUNT;

    private static final ChestValueType[] VALUES = values();

    public static ChestValueType from(int id) {
        return VALUES[id];
    }
}

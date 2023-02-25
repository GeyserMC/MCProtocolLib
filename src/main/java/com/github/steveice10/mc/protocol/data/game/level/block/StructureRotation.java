package com.github.steveice10.mc.protocol.data.game.level.block;

public enum StructureRotation {
    NONE,
    CLOCKWISE_90,
    CLOCKWISE_180,
    COUNTERCLOCKWISE_90;

    private static final StructureRotation[] VALUES = values();

    public static StructureRotation from(int id) {
        return VALUES[id];
    }
}

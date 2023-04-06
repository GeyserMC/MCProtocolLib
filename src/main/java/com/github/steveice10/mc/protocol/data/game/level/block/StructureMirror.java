package com.github.steveice10.mc.protocol.data.game.level.block;

public enum StructureMirror {
    NONE,
    LEFT_RIGHT,
    FRONT_BACK;

    private static final StructureMirror[] VALUES = values();

    public static StructureMirror from(int id) {
        return VALUES[id];
    }
}

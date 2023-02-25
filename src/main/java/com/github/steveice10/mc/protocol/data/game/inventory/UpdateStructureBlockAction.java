package com.github.steveice10.mc.protocol.data.game.inventory;

public enum UpdateStructureBlockAction {
    UPDATE_DATA,
    SAVE_STRUCTURE,
    LOAD_STRUCTURE,
    DETECT_SIZE;

    private static final UpdateStructureBlockAction[] VALUES = values();

    public static UpdateStructureBlockAction from(int id) {
        return VALUES[id];
    }
}

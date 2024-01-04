package org.geysermc.mc.protocol.data.game.inventory;

public enum UpdateStructureBlockMode {
    SAVE,
    LOAD,
    CORNER,
    DATA;

    private static final UpdateStructureBlockMode[] VALUES = values();

    public static UpdateStructureBlockMode from(int id) {
        return VALUES[id];
    }
}

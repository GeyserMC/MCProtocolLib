package org.geysermc.mcprotocollib.protocol.data.game.entity.attribute;

public enum ModifierOperation {
    ADD,
    ADD_MULTIPLIED,
    MULTIPLY;

    private static final ModifierOperation[] VALUES = values();

    public static ModifierOperation from(int id) {
        return VALUES[id];
    }
}

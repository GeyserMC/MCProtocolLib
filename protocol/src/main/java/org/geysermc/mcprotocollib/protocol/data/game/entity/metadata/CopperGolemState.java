package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

public enum CopperGolemState {
    IDLE,
    GETTING_ITEM,
    GETTING_NO_ITEM,
    DROPPING_ITEM,
    DROPPING_NO_ITEM;

    private static final CopperGolemState[] VALUES = values();

    public static CopperGolemState from(int id) {
        return VALUES[id];
    }
}

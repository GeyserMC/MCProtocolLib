package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

public enum BellValueType implements BlockValueType {
    SHAKE_DIRECTION;

    private static final BellValueType[] VALUES = values();

    public static BellValueType from(int id) {
        return VALUES[id];
    }
}

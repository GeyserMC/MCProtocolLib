package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

public enum PotentSulfurValueType implements BlockValueType {
    START_ERUPTION;

    private static final PotentSulfurValueType[] VALUES = values();

    public static PotentSulfurValueType from(int id) {
        return VALUES[id];
    }
}

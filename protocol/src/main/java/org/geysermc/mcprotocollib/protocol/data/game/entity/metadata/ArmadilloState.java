package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

public enum ArmadilloState {
    IDLE,
    ROLLING,
    SCARED,
    UNROLLING;

    private static final ArmadilloState[] VALUES = values();

    public static ArmadilloState from(int id) {
        return VALUES[id];
    }
}

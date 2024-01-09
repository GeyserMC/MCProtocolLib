package org.geysermc.mcprotocollib.protocol.data.game.level.particle.positionsource;

public enum PositionSourceType {
    BLOCK,
    ENTITY;

    private static final PositionSourceType[] VALUES = values();

    public static PositionSourceType from(int id) {
        return VALUES[id];
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.level;

public enum HeightmapTypes {
    WORLD_SURFACE_WG,
    WORLD_SURFACE,
    OCEAN_FLOOR_WG,
    OCEAN_FLOOR,
    MOTION_BLOCKING,
    MOTION_BLOCKING_NO_LEAVES;

    private static final HeightmapTypes[] VALUES = values();

    public static HeightmapTypes from(int id) {
        return VALUES[id];
    }
}

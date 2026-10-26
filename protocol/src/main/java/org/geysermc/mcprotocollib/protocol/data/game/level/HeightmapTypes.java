package org.geysermc.mcprotocollib.protocol.data.game.level;

import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public enum HeightmapTypes {
    WORLD_SURFACE_WG(true),
    WORLD_SURFACE(false),
    OCEAN_FLOOR_WG(true),
    OCEAN_FLOOR(false),
    MOTION_BLOCKING(false),
    MOTION_BLOCKING_NO_LEAVES(false);

    private static final HeightmapTypes[] VALUES = values();

    private final boolean worldGen;

    HeightmapTypes(boolean worldGen) {
        this.worldGen = worldGen;
    }

    public static @Nullable HeightmapTypes from(int id) {
        if (id < 0 || id >= VALUES.length) {
            return null;
        }
        return VALUES[id];
    }
}

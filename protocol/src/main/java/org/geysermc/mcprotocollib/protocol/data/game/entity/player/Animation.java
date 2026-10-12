package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

import org.jspecify.annotations.Nullable;

public enum Animation {
    WAKE_UP,
    CRITICAL_HIT,
    MAGIC_CRITICAL_HIT;

    private static final Animation[] VALUES = values();

    public static @Nullable Animation from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : null;
    }
}

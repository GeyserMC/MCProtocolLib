package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

import org.checkerframework.checker.nullness.qual.Nullable;

public enum Animation {
    WAKE_UP,
    CRITICAL_HIT,
    MAGIC_CRITICAL_HIT;

    private static final Animation[] VALUES = values();

    @Nullable
    public static Animation from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : null;
    }
}

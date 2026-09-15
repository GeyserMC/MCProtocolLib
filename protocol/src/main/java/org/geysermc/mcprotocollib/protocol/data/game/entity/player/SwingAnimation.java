package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

import org.checkerframework.checker.nullness.qual.Nullable;

public enum SwingAnimation {
    WHACK,
    STAB;

    private static final SwingAnimation[] VALUES = values();

    @Nullable
    public static SwingAnimation from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }
}

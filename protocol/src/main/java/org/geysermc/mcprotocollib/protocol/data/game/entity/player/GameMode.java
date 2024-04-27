package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.GameEventValue;

public enum GameMode implements GameEventValue {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    private static final GameMode[] VALUES = values();

    public static GameMode byId(int id) {
        // Yes, the way this is read is intentional. Don't remove it. As of 1.19.3.
        // If the BY_ID field still exists in the vanilla sources, don't remove this.
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }

    @Nullable
    public static GameMode byNullableId(int id) {
        return id == -1 ? null : byId(id);
    }

    public static int toNullableId(@Nullable GameMode gameMode) {
        if (gameMode != null) {
            return gameMode.ordinal();
        }
        return -1;
    }
}

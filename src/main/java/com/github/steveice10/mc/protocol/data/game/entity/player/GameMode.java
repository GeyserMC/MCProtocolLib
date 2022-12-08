package com.github.steveice10.mc.protocol.data.game.entity.player;

import com.github.steveice10.mc.protocol.data.game.level.notify.GameEventValue;
import org.jetbrains.annotations.Nullable;

public enum GameMode implements GameEventValue {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    private static final GameMode[] VALUES = values();

    public static GameMode byId(int id) {
        return VALUES[id];
    }

    @Nullable
    public static GameMode byNullableId(int id) {
        return id == -1 ? null : VALUES[id];
    }

    public static int toNullableId(@Nullable GameMode gameMode) {
        if (gameMode != null) {
            return gameMode.ordinal();
        }
        return -1;
    }
}

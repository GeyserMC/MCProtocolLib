package com.github.steveice10.mc.protocol.data.game.entity.player;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.GlobalPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PlayerSpawnInfo(@NotNull String dimension, @NotNull String worldName, long hashedSeed,
                              @NotNull GameMode gameMode, @Nullable GameMode previousGamemode, boolean debug,
                              boolean flat, @Nullable GlobalPos lastDeathPos, int portalCooldown) {
}

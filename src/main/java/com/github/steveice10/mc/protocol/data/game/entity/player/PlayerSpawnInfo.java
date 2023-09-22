package com.github.steveice10.mc.protocol.data.game.entity.player;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.GlobalPos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class PlayerSpawnInfo {
    private final @NonNull String dimension;
    private final @NonNull String worldName;
    private final long hashedSeed;
    private final @NonNull GameMode gameMode;
    private final @Nullable GameMode previousGamemode;
    private final boolean debug;
    private final boolean flat;
    private final @Nullable GlobalPos lastDeathPos;
    private final int portalCooldown;
}

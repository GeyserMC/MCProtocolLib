package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.GlobalPos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@AllArgsConstructor
public class PlayerSpawnInfo {
    private final int dimension;
    private final @NonNull String worldName;
    private final long hashedSeed;
    private final @NonNull GameMode gameMode;
    private final @Nullable GameMode previousGamemode;
    private final boolean debug;
    private final boolean flat;
    private final @Nullable GlobalPos lastDeathPos;
    private final int portalCooldown;
}

package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ResolvableProfile {
    private final GameProfile profile;
    private final @Nullable Key body;
    private final @Nullable Key cape;
    private final @Nullable Key elytra;
    private final @Nullable GameProfile.TextureModel model;
    private final boolean dynamic;
}

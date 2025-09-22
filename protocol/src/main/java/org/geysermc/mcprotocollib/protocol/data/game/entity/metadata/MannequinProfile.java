package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.ResolvableProfile;

@Data
@With
@AllArgsConstructor
public class MannequinProfile {
    private final @Nullable CustomProfile customProfile;
    private final @Nullable ResolvableProfile profile;

    @Data
    @With
    @AllArgsConstructor
    public static class CustomProfile {
        private final Key texture;
        private final @Nullable Key capeTexture;
        private final @Nullable Key elytraTexture;
        private final GameProfile.TextureModel model;
    }
}

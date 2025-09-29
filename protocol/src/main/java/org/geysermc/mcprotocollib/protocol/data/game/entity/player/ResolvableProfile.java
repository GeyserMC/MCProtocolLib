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

    public ResolvableProfile(GameProfile profile) {
        // A profile is dynamic in Java 1.21.9 when it is missing UUID, name, or properties (empty properties are fine)
        this(profile, null, null, null, null, !profile.isComplete() || profile.getProperties() == null);
    }
}

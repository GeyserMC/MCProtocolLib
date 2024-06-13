package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

public record WolfVariant(Key wildTexture, Key tameTexture, Key angryTexture,
                          @Nullable Key biomeLocation, int @Nullable [] biomeHolders) {
}

package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import org.jetbrains.annotations.Nullable;

public record WolfVariant(String wildTexture, String tameTexture, String angryTexture,
                          @Nullable String biomeLocation, int @Nullable [] biomeHolders) {
}

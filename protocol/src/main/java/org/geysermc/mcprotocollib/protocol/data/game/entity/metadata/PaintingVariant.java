package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public record PaintingVariant(int width, int height, Key assetId, @Nullable Component title, @Nullable Component author) {
}

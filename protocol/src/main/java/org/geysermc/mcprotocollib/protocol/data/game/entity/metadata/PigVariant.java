package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.HolderSet;
import org.jetbrains.annotations.Nullable;

public record PigVariant(int modelId, Key texture, @Nullable HolderSet biomes) {
}

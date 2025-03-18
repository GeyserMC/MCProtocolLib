package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.jetbrains.annotations.Nullable;

@Builder(toBuilder = true)
public record ProvidesTrimMaterial(@Nullable Holder<ArmorTrim.TrimMaterial> materialHolder, @Nullable Key materialLocation) {
}

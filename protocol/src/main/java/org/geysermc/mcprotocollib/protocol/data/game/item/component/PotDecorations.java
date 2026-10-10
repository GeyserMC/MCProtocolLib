package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Builder(toBuilder = true)
public record PotDecorations(@Nullable ItemStack back, @Nullable ItemStack left, @Nullable ItemStack right, @Nullable ItemStack front) {
}

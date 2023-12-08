package com.github.steveice10.mc.protocol.data.game.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import org.jetbrains.annotations.Nullable;

public record VillagerTrade(@Nullable ItemStack firstInput, @Nullable ItemStack secondInput, @Nullable ItemStack output, boolean tradeDisabled, int numUses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
}

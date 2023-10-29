package com.github.steveice10.mc.protocol.data.game.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;

public record VillagerTrade(ItemStack firstInput, ItemStack secondInput, ItemStack output,
                            boolean tradeDisabled,
                            int numUses, int maxUses, int xp,
                            int specialPrice, float priceMultiplier, int demand) {
}

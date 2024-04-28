package org.geysermc.mcprotocollib.protocol.data.game.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@AllArgsConstructor
public class VillagerTrade {
    private final @NonNull ItemStack firstInput;
    private final @Nullable ItemStack secondInput;
    private final @Nullable ItemStack output;
    private final boolean tradeDisabled;
    private final int numUses;
    private final int maxUses;
    private final int xp;
    private final int specialPrice;
    private final float priceMultiplier;
    private final int demand;
}

package org.geysermc.mcprotocollib.protocol.data.game.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;

import java.util.Map;

@Data
@AllArgsConstructor
public class VillagerTrade {
    private final @NonNull ItemCost itemCostA;
    private final @NonNull ItemStack result;
    private final @Nullable ItemCost itemCostB;
    private final boolean outOfStock;
    private final int uses;
    private final int maxUses;
    private final int xp;
    private final int specialPriceDiff;
    private final float priceMultiplier;
    private final int demand;

    public record ItemCost(int itemId, int count, Map<DataComponentType<?>, DataComponent<?, ?>> components) {
    }
}

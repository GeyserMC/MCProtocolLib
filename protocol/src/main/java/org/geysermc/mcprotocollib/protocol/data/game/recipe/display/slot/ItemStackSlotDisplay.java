package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

public record ItemStackSlotDisplay(ItemStack itemStack) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.ITEM_STACK;
    }
}

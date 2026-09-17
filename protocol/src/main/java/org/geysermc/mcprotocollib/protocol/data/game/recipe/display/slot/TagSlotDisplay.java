package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

import org.geysermc.mcprotocollib.protocol.data.game.item.component.HolderSet;

public record TagSlotDisplay(HolderSet holderSet) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.TAG;
    }
}

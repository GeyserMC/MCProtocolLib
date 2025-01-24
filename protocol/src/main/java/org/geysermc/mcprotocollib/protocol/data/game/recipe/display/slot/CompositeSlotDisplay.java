package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

import java.util.List;

public record CompositeSlotDisplay(List<SlotDisplay> contents) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.COMPOSITE;
    }
}

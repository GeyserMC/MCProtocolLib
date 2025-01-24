package org.geysermc.mcprotocollib.protocol.data.game.recipe.display;

import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot.SlotDisplay;

public record FurnaceRecipeDisplay(SlotDisplay ingredient, SlotDisplay fuel, SlotDisplay result, SlotDisplay craftingStation,
                                   int duration, float experience) implements RecipeDisplay {
    @Override
    public RecipeDisplayType getType() {
        return RecipeDisplayType.FURNACE;
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.recipe.display;

import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot.SlotDisplay;

import java.util.List;

public record ShapedCraftingRecipeDisplay(int width, int height, List<SlotDisplay> ingredients,
                                          SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    @Override
    public RecipeDisplayType getType() {
        return RecipeDisplayType.CRAFTING_SHAPED;
    }
}

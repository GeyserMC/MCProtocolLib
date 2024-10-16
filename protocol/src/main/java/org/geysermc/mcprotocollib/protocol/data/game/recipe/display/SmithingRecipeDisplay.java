package org.geysermc.mcprotocollib.protocol.data.game.recipe.display;

import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot.SlotDisplay;

public record SmithingRecipeDisplay(SlotDisplay template, SlotDisplay base, SlotDisplay addition,
                                    SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    @Override
    public RecipeDisplayType getType() {
        return RecipeDisplayType.SMITHING;
    }
}

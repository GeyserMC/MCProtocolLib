package org.geysermc.mcprotocollib.protocol.data.game.recipe.display;

import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot.SlotDisplay;

public interface RecipeDisplay {
    RecipeDisplayType getType();

    SlotDisplay result();
}

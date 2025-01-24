package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

import net.kyori.adventure.key.Key;

public record TagSlotDisplay(Key tag) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.TAG;
    }
}

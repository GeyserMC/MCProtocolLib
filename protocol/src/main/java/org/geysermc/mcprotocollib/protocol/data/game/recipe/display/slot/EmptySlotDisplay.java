package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record EmptySlotDisplay() implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.EMPTY;
    }
}

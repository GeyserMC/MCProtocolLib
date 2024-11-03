package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record EmptySlotDisplay() implements SlotDisplay {
    public static final EmptySlotDisplay INSTANCE = new EmptySlotDisplay();

    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.EMPTY;
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record AnyFuelSlotDisplay() implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.ANY_FUEL;
    }
}

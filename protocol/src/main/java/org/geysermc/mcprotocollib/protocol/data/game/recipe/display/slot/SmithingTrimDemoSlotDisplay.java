package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record SmithingTrimDemoSlotDisplay() implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.SMITHING_TRIM;
    }
}

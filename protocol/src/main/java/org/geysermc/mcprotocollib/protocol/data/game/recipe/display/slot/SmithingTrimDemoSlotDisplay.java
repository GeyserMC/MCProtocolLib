package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record SmithingTrimDemoSlotDisplay(SlotDisplay base, SlotDisplay material, SlotDisplay pattern) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.SMITHING_TRIM;
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record DyedSlotDisplay(SlotDisplay dye, SlotDisplay target) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.DYED;
    }
}

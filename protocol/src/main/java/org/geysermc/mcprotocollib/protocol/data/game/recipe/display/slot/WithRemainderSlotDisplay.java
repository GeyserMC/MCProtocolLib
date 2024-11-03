package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record WithRemainderSlotDisplay(SlotDisplay input, SlotDisplay remainder) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.WITH_REMAINDER;
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record WithAnyPotionSlotDisplay(SlotDisplay display) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.WITH_ANY_POTION;
    }
}

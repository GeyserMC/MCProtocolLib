package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public record ItemSlotDisplay(int item) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.ITEM;
    }
}

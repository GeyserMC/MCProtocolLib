package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;

public record OnlyWithComponentSlotDisplay(SlotDisplay source, DataComponentType<?> component) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.ONLY_WITH_COMPONENT;
    }
}

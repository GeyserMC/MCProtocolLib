package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.ArmorTrim;

public record SmithingTrimDemoSlotDisplay(SlotDisplay base, SlotDisplay material, Holder<ArmorTrim.TrimPattern> pattern) implements SlotDisplay {
    @Override
    public RecipeSlotType getType() {
        return RecipeSlotType.SMITHING_TRIM;
    }
}

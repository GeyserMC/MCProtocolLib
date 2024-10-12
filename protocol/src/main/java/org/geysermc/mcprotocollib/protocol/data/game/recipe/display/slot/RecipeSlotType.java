package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.RecipeDisplayType;

public enum RecipeSlotType {
    EMPTY,
    ANY_FUEL,
    ITEM,
    ITEM_STACK,
    TAG,
    SMITHING_TRIM,
    COMPOSITE;

    private static final RecipeSlotType[] VALUES = values();

    public static RecipeSlotType from(int id) {
        return VALUES[id];
    }
}

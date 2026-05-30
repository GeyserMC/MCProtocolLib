package org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot;

public enum RecipeSlotType {
    EMPTY,
    ANY_FUEL,
    WITH_ANY_POTION,
    ONLY_WITH_COMPONENT,
    ITEM,
    ITEM_STACK,
    TAG,
    DYED,
    SMITHING_TRIM,
    WITH_REMAINDER,
    COMPOSITE;

    private static final RecipeSlotType[] VALUES = values();

    public static RecipeSlotType from(int id) {
        return VALUES[id];
    }
}

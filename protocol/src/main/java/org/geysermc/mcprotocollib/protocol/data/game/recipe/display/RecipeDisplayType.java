package org.geysermc.mcprotocollib.protocol.data.game.recipe.display;

public enum RecipeDisplayType {
    CRAFTING_SHAPELESS,
    CRAFTING_SHAPED,
    FURNACE,
    STONECUTTER,
    SMITHING;

    private static final RecipeDisplayType[] VALUES = values();

    public static RecipeDisplayType from(int id) {
        return VALUES[id];
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.recipe;

public enum BasicRecipeBookCategory {
    CRAFTING_BUILDING_BLOCKS,
    CRAFTING_REDSTONE,
    CRAFTING_EQUIPMENT,
    CRAFTING_MISC,
    FURNACE_FOOD,
    FURNACE_BLOCKS,
    FURNACE_MISC,
    BLAST_FURNACE_BLOCKS,
    BLAST_FURNACE_MISC,
    SMOKER_FOOD,
    STONECUTTER,
    SMITHING,
    CAMPFIRE;

    private static final BasicRecipeBookCategory[] VALUES = values();

    public static BasicRecipeBookCategory from(int id) {
        return VALUES[id];
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.recipe;

public enum CraftingBookCategory {
    BUILDING,
    REDSTONE,
    EQUIPMENT,
    MISC;

    private static final CraftingBookCategory[] VALUES = values();


    public static CraftingBookCategory from(int id) {
        return VALUES[id];
    }
}

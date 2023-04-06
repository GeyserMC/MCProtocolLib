package com.github.steveice10.mc.protocol.data.game.inventory;

public enum CraftingBookStateType {
    CRAFTING,
    FURNACE,
    BLAST_FURNACE,
    SMOKER;

    private static final CraftingBookStateType[] VALUES = values();

    public static CraftingBookStateType from(int id) {
        return VALUES[id];
    }
}

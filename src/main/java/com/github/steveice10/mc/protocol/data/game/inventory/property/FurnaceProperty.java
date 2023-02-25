package com.github.steveice10.mc.protocol.data.game.inventory.property;

/**
 * Container properties of a furnace.
 */
public enum FurnaceProperty implements ContainerProperty {
    /**
     * Number of ticks left before the current fuel runs out.
     */
    BURN_TIME,

    /**
     * Number of ticks that the current item can keep the furnace burning.
     */
    CURRENT_ITEM_BURN_TIME,

    /**
     * Number of ticks the item has been smelting for.
     */
    COOK_TIME,

    /**
     * Number of ticks that the current item needs to be smelted.
     */
    TOTAL_COOK_TIME;

    private static final FurnaceProperty[] VALUES = values();

    public static FurnaceProperty from(int id) {
        return VALUES[id];
    }
}

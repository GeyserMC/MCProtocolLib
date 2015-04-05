package org.spacehq.mc.protocol.data.game.values.window.property;

/**
 * Window properties of a furnace.
 */
public enum FurnaceProperty {

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
    TOTAL_COOK_TIME,
}

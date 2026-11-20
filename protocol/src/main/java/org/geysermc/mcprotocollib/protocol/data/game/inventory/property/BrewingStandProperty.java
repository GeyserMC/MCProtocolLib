package org.geysermc.mcprotocollib.protocol.data.game.inventory.property;

/**
 * Container properties of a brewing stand.
 */
public enum BrewingStandProperty implements ContainerProperty {
    /**
     * Ticks remaining until the potions finish brewing.
     * Counts down from {@link #TOTAL_BREW_TIME} to 0.
     */
    BREW_TIME,

    /**
     * Brewing operations left from the current fuel.
     */
    FUEL_USES,

    /**
     * Ticks the current brewing operation takes in total.
     * Usually 400, or less when the brewing stand brews faster.
     */
    TOTAL_BREW_TIME,

    /**
     * Brewing operations the current fuel provided in total.
     */
    TOTAL_FUEL_USES;

    private static final BrewingStandProperty[] VALUES = values();

    public static BrewingStandProperty from(int id) {
        return VALUES[id];
    }
}

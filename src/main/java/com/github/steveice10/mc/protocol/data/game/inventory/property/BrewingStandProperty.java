package com.github.steveice10.mc.protocol.data.game.inventory.property;

/**
 * Container properties of a brewing stand.
 */
public enum BrewingStandProperty implements ContainerProperty {
    /**
     * Time remaining for potions to finish brewing.
     * Usually a value between 0 (done) and 400 (just started).
     */
    BREW_TIME;

    private static final BrewingStandProperty[] VALUES = values();

    public static BrewingStandProperty from(int id) {
        return VALUES[id];
    }
}

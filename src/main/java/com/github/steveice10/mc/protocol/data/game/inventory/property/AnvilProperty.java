package com.github.steveice10.mc.protocol.data.game.inventory.property;

/**
 * Container properties of an anvil.
 */
public enum AnvilProperty implements ContainerProperty {
    /**
     * The maximum cost of renaming or repairing in the anvil.
     */
    MAXIMUM_COST;

    private static final AnvilProperty[] VALUES = values();

    public static AnvilProperty from(int id) {
        return VALUES[id];
    }
}

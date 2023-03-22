package com.github.steveice10.mc.protocol.data.game.inventory;

public enum ContainerType {
    GENERIC_9X1,
    GENERIC_9X2,
    GENERIC_9X3,
    GENERIC_9X4,
    GENERIC_9X5,
    GENERIC_9X6,
    GENERIC_3X3,
    ANVIL,
    BEACON,
    BLAST_FURNACE,
    BREWING_STAND,
    CRAFTING,
    ENCHANTMENT,
    FURNACE,
    GRINDSTONE,
    HOPPER,
    LECTERN,
    LOOM,
    MERCHANT,
    SHULKER_BOX,
    SMITHING,
    SMOKER,
    CARTOGRAPHY,
    STONECUTTER;

    private static final ContainerType[] VALUES = values();

    public static ContainerType from(int id) {
        return VALUES[id];
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.inventory;

public enum ContainerType {
    GENERIC_9X1,
    GENERIC_9X2,
    GENERIC_9X3,
    GENERIC_9X4,
    GENERIC_9X5,
    GENERIC_9X6,
    GENERIC_3X3,
    CRAFTER_3x3,
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

    /**
     * Returns the container type for the given id, or {@code null} when the
     * id is outside the vanilla range (modded servers, e.g. Waystones, send
     * such ids). Callers that need to log or re-encode the exact id should
     * keep the raw id alongside this lookup — see
     * {@code ClientboundOpenScreenPacket#rawTypeId}.
     */
    public static ContainerType from(int id) {
        if (id < 0 || id >= VALUES.length) {
            return null;
        }
        return VALUES[id];
    }
}

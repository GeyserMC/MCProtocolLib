package org.geysermc.mcprotocollib.protocol.data.game.entity;

public enum EquipmentSlot {
    MAIN_HAND,
    OFF_HAND,
    BOOTS,
    LEGGINGS,
    CHESTPLATE,
    HELMET,
    BODY,
    SADDLE;

    private static final EquipmentSlot[] VALUES = values();

    public static EquipmentSlot fromEnumOrdinal(int id) {
        return VALUES[id];
    }

    public static EquipmentSlot fromId(int networkId) {
        return switch (networkId) {
            case 0 -> MAIN_HAND;
            case 1 -> BOOTS;
            case 2 -> LEGGINGS;
            case 3 -> CHESTPLATE;
            case 4 -> HELMET;
            case 5 -> OFF_HAND;
            case 6 -> BODY;
            case 7 -> SADDLE;
            default -> throw new IllegalStateException("Unexpected equipment slot id: " + networkId);
        };
    }
}

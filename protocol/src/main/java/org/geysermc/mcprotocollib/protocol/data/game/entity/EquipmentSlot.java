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

    public static EquipmentSlot from(int id) {
        return VALUES[id];
    }
}

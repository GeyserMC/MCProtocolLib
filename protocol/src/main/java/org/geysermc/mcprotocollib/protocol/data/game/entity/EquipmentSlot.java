package org.geysermc.mcprotocollib.protocol.data.game.entity;

public enum EquipmentSlot {
    MAIN_HAND(0),
    OFF_HAND(5),
    BOOTS(1),
    LEGGINGS(2),
    CHESTPLATE(3),
    HELMET(4),
    BODY(6),
    SADDLE(7);

    private final int networkId;

    EquipmentSlot(int networkId) {
        this.networkId = networkId;
    }

    public int networkId() {
        return networkId;
    }

    private static final EquipmentSlot[] VALUES = values();
    private static final EquipmentSlot[] BY_NETWORK_ID = new EquipmentSlot[VALUES.length];

    static {
        for (EquipmentSlot slot : VALUES) {
            BY_NETWORK_ID[slot.networkId] = slot;
        }
    }

    public static EquipmentSlot fromEnumOrdinal(int id) {
        return VALUES[id];
    }

    public static EquipmentSlot fromId(int networkId) {
        if (networkId < 0 || networkId >= BY_NETWORK_ID.length) {
            // ByIdMap.OutOfBoundsStrategy.ZERO
            return BY_NETWORK_ID[0];
        }

        return BY_NETWORK_ID[networkId];
    }
}

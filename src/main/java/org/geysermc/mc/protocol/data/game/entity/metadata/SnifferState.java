package org.geysermc.mc.protocol.data.game.entity.metadata;

public enum SnifferState {
    IDLING,
    FEELING_HAPPY,
    SCENTING,
    SNIFFING,
    SEARCHING,
    DIGGING,
    RISING;

    private static final SnifferState[] VALUES = values();

    public static SnifferState from(int id) {
        return VALUES[id];
    }
}

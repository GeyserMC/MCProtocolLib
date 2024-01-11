package org.geysermc.mcprotocollib.protocol.data.handshake;

public enum HandshakeIntent {
    STATUS,
    LOGIN,
    TRANSFER;

    private static final HandshakeIntent[] VALUES = values();

    public static HandshakeIntent from(int id) {
        return VALUES[id];
    }
}

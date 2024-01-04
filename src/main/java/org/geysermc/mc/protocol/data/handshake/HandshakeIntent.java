package org.geysermc.mc.protocol.data.handshake;

public enum HandshakeIntent {
    STATUS,
    LOGIN;

    private static final HandshakeIntent[] VALUES = values();

    public static HandshakeIntent from(int id) {
        return VALUES[id];
    }
}

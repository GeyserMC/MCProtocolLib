package org.geysermc.mcprotocollib.network.packet;

public record FakeFlushPacket() implements Packet {
    public static final FakeFlushPacket INSTANCE = new FakeFlushPacket();
}

package org.geysermc.mcprotocollib.network.example;

import org.geysermc.mcprotocollib.network.codec.CodecByteBuf;
import org.geysermc.mcprotocollib.network.packet.Packet;

public class PingPacket implements Packet {
    private final String id;

    public PingPacket(CodecByteBuf buf) {
        this.id = buf.readString();
    }

    public PingPacket(String id) {
        this.id = id;
    }

    public String getPingId() {
        return this.id;
    }
}

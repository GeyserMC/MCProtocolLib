package org.geysermc.mcprotocollib.network.example;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.packet.Packet;

public class PingPacket implements Packet {
    private final String id;

    public PingPacket(ByteBuf buf, PacketCodecHelper codecHelper) {
        this.id = codecHelper.readString(buf);
    }

    public PingPacket(String id) {
        this.id = id;
    }

    public String getPingId() {
        return this.id;
    }
}

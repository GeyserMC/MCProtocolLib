package org.geysermc.mcprotocollib.network.test;

import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.packet.Packet;
import io.netty.buffer.ByteBuf;

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

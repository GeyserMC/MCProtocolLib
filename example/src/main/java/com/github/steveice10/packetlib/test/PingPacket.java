package com.github.steveice10.packetlib.test;

import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import com.github.steveice10.packetlib.packet.Packet;
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

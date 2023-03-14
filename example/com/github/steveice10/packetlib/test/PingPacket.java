package com.github.steveice10.packetlib.test;

import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class PingPacket implements Packet {
    private final String id;

    public PingPacket(ByteBuf buf, PacketCodecHelper codecHelper) throws IOException {
        this.id = codecHelper.readString(buf);
    }

    public PingPacket(String id) {
        this.id = id;
    }

    public String getPingId() {
        return this.id;
    }

    @Override
    public void write(ByteBuf buf, PacketCodecHelper codecHelper) throws IOException {
        codecHelper.writeString(buf, this.id);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class BufferedPacket implements Packet {
    private final Class<? extends Packet> packetClass;
    private final byte[] buf;

    public BufferedPacket(Class<? extends Packet> packetClass, byte[] buf) {
        this.packetClass = packetClass;
        this.buf = buf;
    }

    public Class<? extends Packet> getPacketClass() {
        return packetClass;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBytes(buf);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}

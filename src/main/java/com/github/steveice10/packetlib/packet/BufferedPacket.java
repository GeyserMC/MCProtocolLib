package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.EOFException;
import java.io.IOException;

public class BufferedPacket implements Packet {
    private Class<? extends Packet> packetClass;
    private byte[] buf;

    public BufferedPacket(Class<? extends Packet> packetClass, byte[] buf) {
        this.packetClass = packetClass;
        this.buf = buf;
    }

    public Class<? extends Packet> getPacketClass() {
        return packetClass;
    }

    @Override
    public void read(NetInput in) throws IOException {
        throw new EOFException("BufferedPacket can not be read");
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

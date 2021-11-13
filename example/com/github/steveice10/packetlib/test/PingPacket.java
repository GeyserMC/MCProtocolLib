package com.github.steveice10.packetlib.test;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class PingPacket implements Packet {
    private final String id;

    public PingPacket(NetInput in) throws IOException {
        this.id = in.readString();
    }

    public PingPacket(String id) {
        this.id = id;
    }

    public String getPingId() {
        return this.id;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.id);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

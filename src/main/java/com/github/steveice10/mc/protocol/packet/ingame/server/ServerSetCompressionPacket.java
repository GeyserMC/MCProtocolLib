package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSetCompressionPacket implements Packet {
    private int threshold;

    @SuppressWarnings("unused")
    private ServerSetCompressionPacket() {
    }

    public ServerSetCompressionPacket(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return this.threshold;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.threshold = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.threshold);
    }

    @Override
    public boolean isPriority() {
        return true;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}

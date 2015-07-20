package org.spacehq.mc.protocol.packet.login.server;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class LoginSetCompressionPacket implements Packet {
    private int threshold;

    @SuppressWarnings("unused")
    private LoginSetCompressionPacket() {
    }

    public LoginSetCompressionPacket(int threshold) {
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
}

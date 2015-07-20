package org.spacehq.mc.protocol.packet.status.server;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class StatusPongPacket implements Packet {

    private long time;

    @SuppressWarnings("unused")
    private StatusPongPacket() {
    }

    public StatusPongPacket(long time) {
        this.time = time;
    }

    public long getPingTime() {
        return this.time;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.time = in.readLong();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeLong(this.time);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}

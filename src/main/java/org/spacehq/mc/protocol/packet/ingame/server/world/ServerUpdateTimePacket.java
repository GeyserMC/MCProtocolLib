package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerUpdateTimePacket implements Packet {

    private long age;
    private long time;

    @SuppressWarnings("unused")
    private ServerUpdateTimePacket() {
    }

    public ServerUpdateTimePacket(long age, long time) {
        this.age = age;
        this.time = time;
    }

    public long getWorldAge() {
        return this.age;
    }

    public long getTime() {
        return this.time;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.age = in.readLong();
        this.time = in.readLong();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeLong(this.age);
        out.writeLong(this.time);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}

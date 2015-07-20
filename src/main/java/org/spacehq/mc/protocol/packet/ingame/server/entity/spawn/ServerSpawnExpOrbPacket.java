package org.spacehq.mc.protocol.packet.ingame.server.entity.spawn;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSpawnExpOrbPacket implements Packet {

    private int entityId;
    private double x;
    private double y;
    private double z;
    private int exp;

    @SuppressWarnings("unused")
    private ServerSpawnExpOrbPacket() {
    }

    public ServerSpawnExpOrbPacket(int entityId, double x, double y, double z, int exp) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.exp = exp;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public int getExp() {
        return this.exp;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.x = in.readInt() / 32D;
        this.y = in.readInt() / 32D;
        this.z = in.readInt() / 32D;
        this.exp = in.readShort();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeInt((int) (this.x * 32));
        out.writeInt((int) (this.y * 32));
        out.writeInt((int) (this.z * 32));
        out.writeShort(this.exp);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}

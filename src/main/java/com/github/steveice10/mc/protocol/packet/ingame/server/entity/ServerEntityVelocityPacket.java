package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ServerEntityVelocityPacket implements Packet {

    private int entityId;
    private double motX;
    private double motY;
    private double motZ;

    @SuppressWarnings("unused")
    private ServerEntityVelocityPacket() {
    }

    public ServerEntityVelocityPacket(int entityId, double motX, double motY, double motZ) {
        this.entityId = entityId;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public double getMotionX() {
        return this.motX;
    }

    public double getMotionY() {
        return this.motY;
    }

    public double getMotionZ() {
        return this.motZ;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.motX = in.readShort() / 8000D;
        this.motY = in.readShort() / 8000D;
        this.motZ = in.readShort() / 8000D;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeShort((int) (this.motX * 8000));
        out.writeShort((int) (this.motY * 8000));
        out.writeShort((int) (this.motZ * 8000));
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}

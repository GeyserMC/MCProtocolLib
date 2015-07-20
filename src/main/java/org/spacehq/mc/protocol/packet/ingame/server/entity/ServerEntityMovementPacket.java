package org.spacehq.mc.protocol.packet.ingame.server.entity;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerEntityMovementPacket implements Packet {

    protected int entityId;
    protected double moveX;
    protected double moveY;
    protected double moveZ;
    protected float yaw;
    protected float pitch;
    private boolean onGround;

    protected boolean pos = false;
    protected boolean rot = false;

    protected ServerEntityMovementPacket() {
    }

    public ServerEntityMovementPacket(int entityId, boolean onGround) {
        this.entityId = entityId;
        this.onGround = onGround;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public double getMovementX() {
        return this.moveX;
    }

    public double getMovementY() {
        return this.moveY;
    }

    public double getMovementZ() {
        return this.moveZ;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        if(this.pos) {
            this.moveX = in.readByte() / 32D;
            this.moveY = in.readByte() / 32D;
            this.moveZ = in.readByte() / 32D;
        }

        if(this.rot) {
            this.yaw = in.readByte() * 360 / 256f;
            this.pitch = in.readByte() * 360 / 256f;
        }

        this.onGround = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        if(this.pos) {
            out.writeByte((int) (this.moveX * 32));
            out.writeByte((int) (this.moveY * 32));
            out.writeByte((int) (this.moveZ * 32));
        }

        if(this.rot) {
            out.writeByte((byte) (this.yaw * 256 / 360));
            out.writeByte((byte) (this.pitch * 256 / 360));
        }

        out.writeBoolean(this.onGround);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}

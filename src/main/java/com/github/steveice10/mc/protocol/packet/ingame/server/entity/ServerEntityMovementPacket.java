package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerEntityMovementPacket extends MinecraftPacket {
    protected int entityId;
    protected double moveX;
    protected double moveY;
    protected double moveZ;
    protected float yaw;
    protected float pitch;
    protected boolean pos = false;
    protected boolean rot = false;
    private boolean onGround;

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
            this.moveX = in.readShort() / 4096D;
            this.moveY = in.readShort() / 4096D;
            this.moveZ = in.readShort() / 4096D;
        }

        if(this.rot) {
            this.yaw = in.readByte() * 360 / 256f;
            this.pitch = in.readByte() * 360 / 256f;
        }

        if(this.pos || this.rot) {
            this.onGround = in.readBoolean();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        if(this.pos) {
            out.writeShort((int) (this.moveX * 4096));
            out.writeShort((int) (this.moveY * 4096));
            out.writeShort((int) (this.moveZ * 4096));
        }

        if(this.rot) {
            out.writeByte((byte) (this.yaw * 256 / 360));
            out.writeByte((byte) (this.pitch * 256 / 360));
        }

        if(this.pos || this.rot) {
            out.writeBoolean(this.onGround);
        }
    }
}

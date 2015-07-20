package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.Particle;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSpawnParticlePacket implements Packet {
    private Particle particle;
    private boolean longDistance;
    private float x;
    private float y;
    private float z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float velocityOffset;
    private int amount;
    private int data[];

    @SuppressWarnings("unused")
    private ServerSpawnParticlePacket() {
    }

    public ServerSpawnParticlePacket(Particle particle, boolean longDistance, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float velocityOffset, int amount, int... data) {
        this.particle = particle;
        this.longDistance = longDistance;
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.velocityOffset = velocityOffset;
        this.amount = amount;
        this.data = data;
        if(this.data.length != particle.getDataLength()) {
            throw new IllegalArgumentException("Data array length must be equal to particle's data length.");
        }
    }

    public Particle getParticle() {
        return this.particle;
    }

    public boolean isLongDistance() {
        return this.longDistance;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public float getOffsetZ() {
        return this.offsetZ;
    }

    public float getVelocityOffset() {
        return this.velocityOffset;
    }

    public int getAmount() {
        return this.amount;
    }

    public int[] getData() {
        return this.data;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.particle = MagicValues.key(Particle.class, in.readInt());
        this.longDistance = in.readBoolean();
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
        this.offsetX = in.readFloat();
        this.offsetY = in.readFloat();
        this.offsetZ = in.readFloat();
        this.velocityOffset = in.readFloat();
        this.amount = in.readInt();
        this.data = new int[this.particle.getDataLength()];
        for(int index = 0; index < this.data.length; index++) {
            this.data[index] = in.readVarInt();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(MagicValues.value(Integer.class, this.particle));
        out.writeBoolean(this.longDistance);
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.z);
        out.writeFloat(this.offsetX);
        out.writeFloat(this.offsetY);
        out.writeFloat(this.offsetZ);
        out.writeFloat(this.velocityOffset);
        out.writeInt(this.amount);
        for(int index = 0; index < this.particle.getDataLength(); index++) {
            out.writeVarInt(this.data[index]);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

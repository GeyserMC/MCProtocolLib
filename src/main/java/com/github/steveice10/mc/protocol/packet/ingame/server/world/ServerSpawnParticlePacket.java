package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.world.particle.Particle;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleData;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerSpawnParticlePacket implements Packet {
    private @NonNull Particle particle;
    private boolean longDistance;
    private double x;
    private double y;
    private double z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float velocityOffset;
    private int amount;

    @Override
    public void read(NetInput in) throws IOException {
        ParticleType type = ParticleType.VALUES[in.readInt()];
        this.longDistance = in.readBoolean();
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.offsetX = in.readFloat();
        this.offsetY = in.readFloat();
        this.offsetZ = in.readFloat();
        this.velocityOffset = in.readFloat();
        this.amount = in.readInt();
        this.particle = new Particle(type, ParticleData.read(in, type));
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(particle.getType().ordinal());
        out.writeBoolean(this.longDistance);
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.offsetX);
        out.writeFloat(this.offsetY);
        out.writeFloat(this.offsetZ);
        out.writeFloat(this.velocityOffset);
        out.writeInt(this.amount);
        ParticleData.write(out, this.particle.getType(), this.particle.getData());
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

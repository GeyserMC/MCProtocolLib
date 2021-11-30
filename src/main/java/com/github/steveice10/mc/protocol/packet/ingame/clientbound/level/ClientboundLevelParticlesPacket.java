package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.level.particle.Particle;
import com.github.steveice10.mc.protocol.data.game.level.particle.ParticleData;
import com.github.steveice10.mc.protocol.data.game.level.particle.ParticleType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelParticlesPacket implements Packet {
    private final @NonNull Particle particle;
    private final boolean longDistance;
    private final double x;
    private final double y;
    private final double z;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float velocityOffset;
    private final int amount;

    public ClientboundLevelParticlesPacket(NetInput in) throws IOException {
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
}

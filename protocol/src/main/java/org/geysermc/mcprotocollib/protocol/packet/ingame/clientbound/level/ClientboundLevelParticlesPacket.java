package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ParticleType;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelParticlesPacket implements MinecraftPacket {
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

    public ClientboundLevelParticlesPacket(MinecraftByteBuf buf) {
        this.longDistance = buf.readBoolean();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.offsetX = buf.readFloat();
        this.offsetY = buf.readFloat();
        this.offsetZ = buf.readFloat();
        this.velocityOffset = buf.readFloat();
        this.amount = buf.readInt();
        ParticleType type = buf.readParticleType();
        this.particle = new Particle(type, buf.readParticleData(type));
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeBoolean(this.longDistance);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.offsetX);
        buf.writeFloat(this.offsetY);
        buf.writeFloat(this.offsetZ);
        buf.writeFloat(this.velocityOffset);
        buf.writeInt(this.amount);
        buf.writeParticleType(this.particle.getType());
        buf.writeParticleData(this.particle.getType(), this.particle.getData());
    }
}

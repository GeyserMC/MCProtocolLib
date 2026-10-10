package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ParticleType;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelParticlesPacket implements MinecraftPacket {
    private final @NonNull Particle particle;
    private final boolean longDistance;
    private final boolean alwaysShow;
    private final double x;
    private final double y;
    private final double z;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float xMaxSpeed;
    private final float yMaxSpeed;
    private final float zMaxSpeed;
    private final int amount;
    private final RandomizationType randomizationType;

    public ClientboundLevelParticlesPacket(ByteBuf in) {
        ParticleType type = MinecraftTypes.readParticleType(in);
        this.particle = new Particle(type, MinecraftTypes.readParticleData(in, type));
        this.longDistance = in.readBoolean();
        this.alwaysShow = in.readBoolean();
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.offsetX = in.readFloat();
        this.offsetY = in.readFloat();
        this.offsetZ = in.readFloat();
        this.xMaxSpeed = in.readFloat();
        this.yMaxSpeed = in.readFloat();
        this.zMaxSpeed = in.readFloat();
        this.amount = MinecraftTypes.readVarInt(in);
        this.randomizationType = RandomizationType.from(MinecraftTypes.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeParticleType(out, this.particle.getType());
        MinecraftTypes.writeParticleData(out, this.particle.getType(), this.particle.getData());
        out.writeBoolean(this.longDistance);
        out.writeBoolean(this.alwaysShow);
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.offsetX);
        out.writeFloat(this.offsetY);
        out.writeFloat(this.offsetZ);
        out.writeFloat(this.xMaxSpeed);
        out.writeFloat(this.yMaxSpeed);
        out.writeFloat(this.zMaxSpeed);
        MinecraftTypes.writeVarInt(out, this.amount);
        MinecraftTypes.writeVarInt(out, this.randomizationType.ordinal());
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }

    public enum RandomizationType {
        DEFAULT,
        ALTERNATIVE,
        ALTERNATIVE_WITH_SPEED;

        private static final RandomizationType[] VALUES = values();

        @Nullable
        public static RandomizationType from(int id) {
            return id >= 0 && id < VALUES.length ? VALUES[id] : null;
        }
    }
}

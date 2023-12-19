package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.block.ExplosionInteraction;
import com.github.steveice10.mc.protocol.data.game.level.particle.Particle;
import com.github.steveice10.mc.protocol.data.game.level.sound.Sound;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundExplodePacket implements MinecraftPacket {
    private final double x;
    private final double y;
    private final double z;
    private final float radius;
    private final @NonNull List<Vector3i> exploded;
    private final float pushX;
    private final float pushY;
    private final float pushZ;
    private final @NonNull Particle smallExplosionParticles;
    private final @NonNull Particle largeExplosionParticles;
    private final @NonNull ExplosionInteraction blockInteraction;
    private final @NonNull Sound explosionSound;

    public ClientboundExplodePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.radius = in.readFloat();
        this.exploded = new ArrayList<>();
        int length = helper.readVarInt(in);
        for (int count = 0; count < length; count++) {
            this.exploded.add(Vector3i.from(in.readByte(), in.readByte(), in.readByte()));
        }

        this.pushX = in.readFloat();
        this.pushY = in.readFloat();
        this.pushZ = in.readFloat();
        this.blockInteraction = ExplosionInteraction.from(helper.readVarInt(in)); // different order than mojang fields
        this.smallExplosionParticles = helper.readParticle(in);
        this.largeExplosionParticles = helper.readParticle(in);
        this.explosionSound = helper.readSoundEvent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.radius);
        helper.writeVarInt(out, this.exploded.size());
        for (Vector3i record : this.exploded) {
            out.writeByte(record.getX());
            out.writeByte(record.getY());
            out.writeByte(record.getZ());
        }

        out.writeFloat(this.pushX);
        out.writeFloat(this.pushY);
        out.writeFloat(this.pushZ);
        helper.writeVarInt(out, this.blockInteraction.ordinal()); // different order than mojang fields
        helper.writeParticle(out, this.smallExplosionParticles);
        helper.writeParticle(out, this.largeExplosionParticles);
        helper.writeSoundEvent(out, this.explosionSound);
    }
}

package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.ExplosionInteraction;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.BuiltinSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.CustomSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

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

    public ClientboundExplodePacket(MinecraftByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.radius = buf.readFloat();
        this.exploded = new ArrayList<>();
        int length = buf.readVarInt();
        for (int count = 0; count < length; count++) {
            this.exploded.add(Vector3i.from(buf.readByte(), buf.readByte(), buf.readByte()));
        }

        this.pushX = buf.readFloat();
        this.pushY = buf.readFloat();
        this.pushZ = buf.readFloat();
        this.blockInteraction = ExplosionInteraction.from(buf.readVarInt()); // different order than mojang fields
        this.smallExplosionParticles = buf.readParticle();
        this.largeExplosionParticles = buf.readParticle();
        this.explosionSound = buf.readById(BuiltinSound::from, buf::readSoundEvent);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.radius);
        buf.writeVarInt(this.exploded.size());
        for (Vector3i record : this.exploded) {
            buf.writeByte(record.getX());
            buf.writeByte(record.getY());
            buf.writeByte(record.getZ());
        }

        buf.writeFloat(this.pushX);
        buf.writeFloat(this.pushY);
        buf.writeFloat(this.pushZ);
        buf.writeVarInt(this.blockInteraction.ordinal()); // different order than mojang fields
        buf.writeParticle(this.smallExplosionParticles);
        buf.writeParticle(this.largeExplosionParticles);
        if (this.explosionSound instanceof CustomSound) {
            buf.writeVarInt(0);
            buf.writeSoundEvent(this.explosionSound);
        } else {
            buf.writeVarInt(((BuiltinSound) this.explosionSound).ordinal() + 1);
        }
    }
}

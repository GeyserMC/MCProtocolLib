package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.BuiltinSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.CustomSound;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundExplodePacket implements MinecraftPacket {
    private final Vector3d center;
    private final @Nullable Vector3d playerKnockback;
    private final @NonNull Particle explosionParticle;
    private final @NonNull Sound explosionSound;

    public ClientboundExplodePacket(ByteBuf in) {
        this.center = Vector3d.from(in.readDouble(), in.readDouble(), in.readDouble());
        this.playerKnockback = MinecraftTypes.readNullable(in, (input) -> Vector3d.from(input.readDouble(), input.readDouble(), input.readDouble()));
        this.explosionParticle = MinecraftTypes.readParticle(in);
        this.explosionSound = MinecraftTypes.readById(in, BuiltinSound::from, MinecraftTypes::readSoundEvent);
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeDouble(this.center.getX());
        out.writeDouble(this.center.getY());
        out.writeDouble(this.center.getZ());
        MinecraftTypes.writeNullable(out, this.playerKnockback, (output, playerKnockback) -> {
            output.writeDouble(playerKnockback.getX());
            output.writeDouble(playerKnockback.getY());
            output.writeDouble(playerKnockback.getZ());
        });
        MinecraftTypes.writeParticle(out, this.explosionParticle);
        if (this.explosionSound instanceof CustomSound) {
            MinecraftTypes.writeVarInt(out, 0);
            MinecraftTypes.writeSoundEvent(out, this.explosionSound);
        } else {
            MinecraftTypes.writeVarInt(out, ((BuiltinSound) this.explosionSound).ordinal() + 1);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}

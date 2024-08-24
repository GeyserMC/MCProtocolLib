package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
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

    public ClientboundExplodePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.center = Vector3d.from(in.readDouble(), in.readDouble(), in.readDouble());
        this.playerKnockback = helper.readNullable(in, (input) -> Vector3d.from(input.readDouble(), input.readDouble(), input.readDouble()));
        this.explosionParticle = helper.readParticle(in);
        this.explosionSound = helper.readById(in, BuiltinSound::from, helper::readSoundEvent);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeDouble(this.center.getX());
        out.writeDouble(this.center.getY());
        out.writeDouble(this.center.getZ());
        helper.writeNullable(out, this.playerKnockback, (output, playerKnockback) -> {
            output.writeDouble(playerKnockback.getX());
            output.writeDouble(playerKnockback.getY());
            output.writeDouble(playerKnockback.getZ());
        });
        helper.writeParticle(out, this.explosionParticle);
        if (this.explosionSound instanceof CustomSound) {
            helper.writeVarInt(out, 0);
            helper.writeSoundEvent(out, this.explosionSound);
        } else {
            helper.writeVarInt(out, ((BuiltinSound) this.explosionSound).ordinal() + 1);
        }
    }
}

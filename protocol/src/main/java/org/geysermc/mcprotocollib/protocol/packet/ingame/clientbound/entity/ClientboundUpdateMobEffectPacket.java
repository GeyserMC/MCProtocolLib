package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateMobEffectPacket implements MinecraftPacket {
    private static final int FLAG_AMBIENT = 0x01;
    private static final int FLAG_SHOW_PARTICLES = 0x02;
    private static final int FLAG_SHOW_ICON = 0x04;
    private static final int FLAG_BLEND = 0x08;

    private final int entityId;
    private final @NonNull Effect effect;
    private final int amplifier;
    private final int duration;
    private final boolean ambient;
    private final boolean showParticles;
    private final boolean showIcon;
    private final boolean blend;

    public ClientboundUpdateMobEffectPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.effect = MinecraftTypes.readEffect(in);
        this.amplifier = MinecraftTypes.readVarInt(in);
        this.duration = MinecraftTypes.readVarInt(in);

        int flags = in.readByte();
        this.ambient = (flags & FLAG_AMBIENT) != 0;
        this.showParticles = (flags & FLAG_SHOW_PARTICLES) != 0;
        this.showIcon = (flags & FLAG_SHOW_ICON) != 0;
        this.blend = (flags & FLAG_BLEND) != 0;
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeEffect(out, this.effect);
        MinecraftTypes.writeVarInt(out, this.amplifier);
        MinecraftTypes.writeVarInt(out, this.duration);

        int flags = 0;
        if (this.ambient) {
            flags |= FLAG_AMBIENT;
        }
        if (this.showParticles) {
            flags |= FLAG_SHOW_PARTICLES;
        }
        if (this.showIcon) {
            flags |= FLAG_SHOW_ICON;
        }
        if (this.blend) {
            flags |= FLAG_BLEND;
        }

        out.writeByte(flags);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}

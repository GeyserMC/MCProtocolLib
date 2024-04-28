package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

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

    public ClientboundUpdateMobEffectPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = helper.readVarInt(in);
        this.effect = helper.readEffect(in);
        this.amplifier = helper.readVarInt(in);
        this.duration = helper.readVarInt(in);

        int flags = in.readByte();
        this.ambient = (flags & FLAG_AMBIENT) != 0;
        this.showParticles = (flags & FLAG_SHOW_PARTICLES) != 0;
        this.showIcon = (flags & FLAG_SHOW_ICON) != 0;
        this.blend = (flags & FLAG_BLEND) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.entityId);
        helper.writeEffect(out, this.effect);
        helper.writeVarInt(out, this.amplifier);
        helper.writeVarInt(out, this.duration);

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
}

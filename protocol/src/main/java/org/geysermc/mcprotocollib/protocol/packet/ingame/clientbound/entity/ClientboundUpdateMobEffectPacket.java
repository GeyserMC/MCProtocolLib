package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
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

    public ClientboundUpdateMobEffectPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.effect = buf.readEffect();
        this.amplifier = buf.readVarInt();
        this.duration = buf.readVarInt();

        int flags = buf.readByte();
        this.ambient = (flags & FLAG_AMBIENT) != 0;
        this.showParticles = (flags & FLAG_SHOW_PARTICLES) != 0;
        this.showIcon = (flags & FLAG_SHOW_ICON) != 0;
        this.blend = (flags & FLAG_BLEND) != 0;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeEffect(this.effect);
        buf.writeVarInt(this.amplifier);
        buf.writeVarInt(this.duration);

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

        buf.writeByte(flags);
    }
}

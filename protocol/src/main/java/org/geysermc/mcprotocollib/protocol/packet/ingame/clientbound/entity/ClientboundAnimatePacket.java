package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Animation;

@Data
@With
@AllArgsConstructor
public class ClientboundAnimatePacket implements MinecraftPacket {
    private final int entityId;
    private final @Nullable Animation animation;

    public ClientboundAnimatePacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.animation = Animation.from(buf.readUnsignedByte());
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        if (this.animation == null) {
            buf.writeByte(-1); // Client does nothing on unknown ID
        } else {
            buf.writeByte(this.animation.getId());
        }
    }
}

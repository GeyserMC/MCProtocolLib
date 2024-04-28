package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Animation;

@Data
@With
@AllArgsConstructor
public class ClientboundAnimatePacket implements MinecraftPacket {
    private final int entityId;
    private final @Nullable Animation animation;

    public ClientboundAnimatePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = helper.readVarInt(in);
        this.animation = Animation.from(in.readUnsignedByte());
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.entityId);
        if (this.animation == null) {
            out.writeByte(-1); // Client does nothing on unknown ID
        } else {
            out.writeByte(this.animation.getId());
        }
    }
}

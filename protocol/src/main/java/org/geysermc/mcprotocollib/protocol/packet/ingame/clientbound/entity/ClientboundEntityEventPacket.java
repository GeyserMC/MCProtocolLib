package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEvent;

@Data
@With
@AllArgsConstructor
public class ClientboundEntityEventPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull EntityEvent event;

    public ClientboundEntityEventPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = in.readInt();
        this.event = helper.readEntityEvent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeInt(this.entityId);
        helper.writeEntityEvent(out, this.event);
    }
}

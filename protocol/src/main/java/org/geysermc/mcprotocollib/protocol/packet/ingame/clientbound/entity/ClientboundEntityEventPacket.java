package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEvent;

@Data
@With
@AllArgsConstructor
public class ClientboundEntityEventPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull EntityEvent event;

    public ClientboundEntityEventPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readInt();
        this.event = buf.readEntityEvent();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeEntityEvent(this.event);
    }
}

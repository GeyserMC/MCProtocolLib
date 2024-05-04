package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEntityLinkPacket implements MinecraftPacket {
    private final int entityId;
    private final int attachedToId;

    public ClientboundSetEntityLinkPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readInt();
        this.attachedToId = buf.readInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.attachedToId);
    }
}

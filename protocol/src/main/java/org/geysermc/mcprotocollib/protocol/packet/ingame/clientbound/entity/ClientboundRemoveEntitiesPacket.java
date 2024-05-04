package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundRemoveEntitiesPacket implements MinecraftPacket {
    private final int @NonNull [] entityIds;

    public ClientboundRemoveEntitiesPacket(MinecraftByteBuf buf) {
        this.entityIds = new int[buf.readVarInt()];
        for (int i = 0; i < this.entityIds.length; i++) {
            this.entityIds[i] = buf.readVarInt();
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityIds.length);
        for (int entityId : this.entityIds) {
            buf.writeVarInt(entityId);
        }
    }
}

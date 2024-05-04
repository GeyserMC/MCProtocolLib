package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEntityDataPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull EntityMetadata<?, ?>[] metadata;

    public ClientboundSetEntityDataPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.metadata = buf.readEntityMetadata();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeEntityMetadata(this.metadata);
    }
}

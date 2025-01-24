package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEntityDataPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull EntityMetadata<?, ?>[] metadata;

    public ClientboundSetEntityDataPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.metadata = MinecraftTypes.readEntityMetadata(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeEntityMetadata(out, this.metadata);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}

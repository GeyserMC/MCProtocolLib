package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundRemoveEntitiesPacket implements MinecraftPacket {
    private final int @NonNull [] entityIds;

    public ClientboundRemoveEntitiesPacket(ByteBuf in) {
        this.entityIds = new int[MinecraftTypes.readVarInt(in)];
        for (int i = 0; i < this.entityIds.length; i++) {
            this.entityIds[i] = MinecraftTypes.readVarInt(in);
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityIds.length);
        for (int entityId : this.entityIds) {
            MinecraftTypes.writeVarInt(out, entityId);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}

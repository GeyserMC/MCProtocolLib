package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundChunkBatchFinishedPacket implements MinecraftPacket {
    private final int batchSize;

    public ClientboundChunkBatchFinishedPacket(MinecraftByteBuf buf) {
        this.batchSize = buf.readVarInt();
    }

    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.batchSize);
    }
}

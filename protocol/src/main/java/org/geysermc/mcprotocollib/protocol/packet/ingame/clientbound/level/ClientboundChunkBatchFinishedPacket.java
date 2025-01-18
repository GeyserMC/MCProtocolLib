package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundChunkBatchFinishedPacket implements MinecraftPacket {
    private final int batchSize;

    public ClientboundChunkBatchFinishedPacket(ByteBuf in) {
        this.batchSize = MinecraftTypes.readVarInt(in);
    }

    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.batchSize);
    }
}

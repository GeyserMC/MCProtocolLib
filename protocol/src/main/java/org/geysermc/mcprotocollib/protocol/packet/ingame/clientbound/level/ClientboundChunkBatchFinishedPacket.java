package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundChunkBatchFinishedPacket implements MinecraftPacket {
    private final int batchSize;

    public ClientboundChunkBatchFinishedPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.batchSize = helper.readVarInt(in);
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.batchSize);
    }
}

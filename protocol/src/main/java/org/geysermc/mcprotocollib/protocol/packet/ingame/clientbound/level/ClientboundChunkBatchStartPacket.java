package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ClientboundChunkBatchStartPacket implements MinecraftPacket {

    public ClientboundChunkBatchStartPacket(ByteBuf in) {
    }

    public void serialize(ByteBuf out) {
    }
}

package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor
public class ClientboundChunkBatchStartPacket implements MinecraftPacket {

    public ClientboundChunkBatchStartPacket(MinecraftByteBuf buf) {
    }

    public void serialize(MinecraftByteBuf buf) {
    }
}

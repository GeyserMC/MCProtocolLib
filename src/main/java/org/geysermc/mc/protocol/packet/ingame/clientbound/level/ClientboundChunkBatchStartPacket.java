package org.geysermc.mc.protocol.packet.ingame.clientbound.level;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientboundChunkBatchStartPacket implements MinecraftPacket {

    public ClientboundChunkBatchStartPacket(ByteBuf in, MinecraftCodecHelper helper) {
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
    }
}

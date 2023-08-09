package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundChunkBatchFinishedPacket implements MinecraftPacket {
    private final int batchSize;

    public ClientboundChunkBatchFinishedPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.batchSize = helper.readVarInt(in);
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.batchSize);
    }
}

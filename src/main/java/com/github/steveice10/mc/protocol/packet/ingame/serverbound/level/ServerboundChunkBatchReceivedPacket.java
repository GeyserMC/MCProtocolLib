package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundChunkBatchReceivedPacket implements MinecraftPacket {
    private final float desiredBatchSize;

    public ServerboundChunkBatchReceivedPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.desiredBatchSize = in.readFloat();
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeFloat(this.desiredBatchSize);
    }
}

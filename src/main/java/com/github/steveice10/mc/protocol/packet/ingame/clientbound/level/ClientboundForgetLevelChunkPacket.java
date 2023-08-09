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
public class ClientboundForgetLevelChunkPacket implements MinecraftPacket {
    private final int x;
    private final int z;

    public ClientboundForgetLevelChunkPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        long chunkPosition = in.readLong();
        this.x = (int)chunkPosition;
        this.z = (int)(chunkPosition >> 32);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeLong(this.x & 0xFFFFFFFFL | (this.z & 0xFFFFFFFFL) << 32);
    }
}

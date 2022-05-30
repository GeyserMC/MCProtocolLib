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
public class ClientboundSetChunkCacheCenterPacket implements MinecraftPacket {
    private final int chunkX;
    private final int chunkZ;

    public ClientboundSetChunkCacheCenterPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.chunkX = helper.readVarInt(in);
        this.chunkZ = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.chunkX);
        helper.writeVarInt(out, this.chunkZ);
    }
}

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
public class ClientboundSetChunkCacheCenterPacket implements MinecraftPacket {
    private final int chunkX;
    private final int chunkZ;

    public ClientboundSetChunkCacheCenterPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.chunkX = helper.readVarInt(in);
        this.chunkZ = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.chunkX);
        helper.writeVarInt(out, this.chunkZ);
    }
}

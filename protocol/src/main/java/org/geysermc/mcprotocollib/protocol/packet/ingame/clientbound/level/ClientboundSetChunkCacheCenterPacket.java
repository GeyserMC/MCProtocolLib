package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetChunkCacheCenterPacket implements MinecraftPacket {
    private final int chunkX;
    private final int chunkZ;

    public ClientboundSetChunkCacheCenterPacket(MinecraftByteBuf buf) {
        this.chunkX = buf.readVarInt();
        this.chunkZ = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.chunkX);
        buf.writeVarInt(this.chunkZ);
    }
}

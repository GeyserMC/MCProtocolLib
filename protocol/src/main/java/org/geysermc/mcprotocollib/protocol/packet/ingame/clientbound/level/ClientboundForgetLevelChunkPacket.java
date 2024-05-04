package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundForgetLevelChunkPacket implements MinecraftPacket {
    private final int x;
    private final int z;

    public ClientboundForgetLevelChunkPacket(MinecraftByteBuf buf) {
        long chunkPosition = buf.readLong();
        this.x = (int) chunkPosition;
        this.z = (int) (chunkPosition >> 32);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeLong(this.x & 0xFFFFFFFFL | (this.z & 0xFFFFFFFFL) << 32);
    }
}

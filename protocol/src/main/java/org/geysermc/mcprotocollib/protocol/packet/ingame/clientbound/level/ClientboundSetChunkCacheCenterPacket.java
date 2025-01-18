package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetChunkCacheCenterPacket implements MinecraftPacket {
    private final int chunkX;
    private final int chunkZ;

    public ClientboundSetChunkCacheCenterPacket(ByteBuf in) {
        this.chunkX = MinecraftTypes.readVarInt(in);
        this.chunkZ = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.chunkX);
        MinecraftTypes.writeVarInt(out, this.chunkZ);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}

package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.ChunkBiomeData;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ClientboundChunksBiomesPacket implements MinecraftPacket {
    private final List<ChunkBiomeData> chunkBiomeData;

    public ClientboundChunksBiomesPacket(MinecraftByteBuf buf) {
        this.chunkBiomeData = new ArrayList<>();

        int length = buf.readVarInt();
        for (int i = 0; i < length; i++) {
            long raw = buf.readLong();
            this.chunkBiomeData.add(new ChunkBiomeData((int) raw, (int) (raw >> 32), buf.readByteArray()));
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.chunkBiomeData.size());
        for (ChunkBiomeData entry : this.chunkBiomeData) {
            long raw = (long) entry.getX() & 0xFFFFFFFFL | ((long) entry.getZ() & 0xFFFFFFFFL) << 32;
            buf.writeLong(raw);
            buf.writeByteArray(entry.getBuffer());
        }
    }
}

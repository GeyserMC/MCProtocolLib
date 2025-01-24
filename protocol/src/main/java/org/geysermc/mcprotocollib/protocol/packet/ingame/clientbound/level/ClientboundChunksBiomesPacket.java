package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.ChunkBiomeData;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientboundChunksBiomesPacket implements MinecraftPacket {
    private final List<ChunkBiomeData> chunkBiomeData;

    public ClientboundChunksBiomesPacket(ByteBuf in) {
        this.chunkBiomeData = MinecraftTypes.readList(in, buf -> {
            long raw = buf.readLong();
            return new ChunkBiomeData((int) raw, (int) (raw >> 32), MinecraftTypes.readByteArray(buf));
        });
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeList(out, this.chunkBiomeData, (buf, entry) -> {
            long raw = (long) entry.getX() & 0xFFFFFFFFL | ((long) entry.getZ() & 0xFFFFFFFFL) << 32;
            buf.writeLong(raw);
            MinecraftTypes.writeByteArray(buf, entry.getBuffer());
        });
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}

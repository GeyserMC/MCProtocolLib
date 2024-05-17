package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.ChunkBiomeData;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientboundChunksBiomesPacket implements MinecraftPacket {
    private final List<ChunkBiomeData> chunkBiomeData;

    public ClientboundChunksBiomesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.chunkBiomeData = helper.readList(in, buf -> {
            long raw = buf.readLong();
            return new ChunkBiomeData((int) raw, (int) (raw >> 32), helper.readByteArray(buf));
        });
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeList(out, this.chunkBiomeData, (buf, entry) -> {
            long raw = (long) entry.getX() & 0xFFFFFFFFL | ((long) entry.getZ() & 0xFFFFFFFFL) << 32;
            buf.writeLong(raw);
            helper.writeByteArray(buf, entry.getBuffer());
        });
    }
}

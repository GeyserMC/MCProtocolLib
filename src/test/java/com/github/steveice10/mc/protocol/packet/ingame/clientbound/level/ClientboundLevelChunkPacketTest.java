package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.chunk.ChunkSection;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import org.junit.Before;

public class ClientboundLevelChunkPacketTest extends PacketTest {
    @Before
    public void setup() {
        ChunkSection chunk = new ChunkSection();
        chunk.set(0, 0, 0, 10);

        this.setPackets(
                new ClientboundLevelChunkPacket(
                        new Chunk(0, 0, new ChunkSection[]{
                                null, null, null, null, null, null, null, chunk,
                                null, chunk, null, null, null, chunk, null, null, null, null, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null
                        }, new CompoundTag[0], new CompoundTag("HeightMaps"), new int[1024])
                ),
                new ClientboundLevelChunkPacket(
                        new Chunk(1, 1, new ChunkSection[]{
                                chunk, chunk, chunk, chunk, chunk, chunk, chunk, chunk,
                                chunk, chunk, chunk, chunk, chunk, chunk, chunk, chunk, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
                        }, new CompoundTag[]{
                                new CompoundTag("TileEntity")
                        }, new CompoundTag("HeightMaps"), new int[1024])
                )
        );
    }
}

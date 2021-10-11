package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import org.junit.Before;

public class ServerChunkDataPacketTest extends PacketTest {
    @Before
    public void setup() {
        Chunk chunk = new Chunk();
        chunk.set(0, 0, 0, 10);

        this.setPackets(
                new ServerChunkDataPacket(
                        new Column(0, 0, new Chunk[]{
                                null, null, null, null, null, null, null, chunk,
                                null, chunk, null, null, null, chunk, null, null, null, null, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                                null, null, null, null, null, null, null, null, null, null, null
                        }, new CompoundTag[0], new CompoundTag("HeightMaps"), new int[1024])
                ),
                new ServerChunkDataPacket(
                        new Column(1, 1, new Chunk[]{
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

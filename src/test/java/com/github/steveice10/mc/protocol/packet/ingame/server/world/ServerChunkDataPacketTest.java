package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.junit.Before;

public class ServerChunkDataPacketTest extends PacketTest {
    @Before
    public void setup() {
        Chunk chunk = new Chunk();
        chunk.set(0, 0, 0, 10);

        this.setPackets(
                new ServerChunkDataPacket(
                        new Column(0, 0, new Chunk[] {
                                null, null, null, null, null, null, null, chunk,
                                null, chunk, null, null, null, chunk, null, null
                        }, new CompoundBinaryTag[0], CompoundBinaryTag.empty())
                ),
                new ServerChunkDataPacket(
                        new Column(1, 1, new Chunk[] {
                                chunk, chunk, chunk, chunk, chunk, chunk, chunk, chunk,
                                chunk, chunk, chunk, chunk, chunk, chunk, chunk, chunk
                        }, new CompoundBinaryTag[] {
                                CompoundBinaryTag.empty()
                        }, CompoundBinaryTag.empty(), new int[1024])
                )
        );
    }
}

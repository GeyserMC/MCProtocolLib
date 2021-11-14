package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.level.LightUpdateData;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityInfo;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import org.junit.Before;

import java.io.IOException;
import java.util.BitSet;
import java.util.Collections;

public class ClientboundLevelChunkWithLightPacketTest extends PacketTest {
    @Before
    public void setup() throws IOException {
        Chunk chunk = new Chunk();
        chunk.setBlock(0, 0, 0, 10);

        this.setPackets(
                new ClientboundLevelChunkWithLightPacket(0, 0,
                        new byte[0], new CompoundTag("HeightMaps"), new BlockEntityInfo[0],
                        new LightUpdateData(new BitSet(), new BitSet(), new BitSet(), new BitSet(), Collections.emptyList(), Collections.emptyList(), false)
                ),
                new ClientboundLevelChunkWithLightPacket(1, 1,
                        new byte[256], new CompoundTag("HeightMaps"), new BlockEntityInfo[]{
                                new BlockEntityInfo(1, 0, 1, 0, null)
                        }, new LightUpdateData(new BitSet(), new BitSet(), new BitSet(), new BitSet(), Collections.emptyList(), Collections.emptyList(), true)
                )
        );
    }
}

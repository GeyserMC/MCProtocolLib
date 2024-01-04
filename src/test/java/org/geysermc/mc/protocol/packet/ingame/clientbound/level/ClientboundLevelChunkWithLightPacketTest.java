package org.geysermc.mc.protocol.packet.ingame.clientbound.level;

import org.geysermc.mc.protocol.data.game.level.LightUpdateData;
import org.geysermc.mc.protocol.data.game.level.block.BlockEntityInfo;
import org.geysermc.mc.protocol.data.game.level.block.BlockEntityType;
import org.geysermc.mc.protocol.packet.PacketTest;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import org.junit.jupiter.api.BeforeEach;

import java.util.BitSet;
import java.util.Collections;

public class ClientboundLevelChunkWithLightPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(
                new ClientboundLevelChunkWithLightPacket(0, 0,
                        new byte[0], new CompoundTag(""), new BlockEntityInfo[0],
                        new LightUpdateData(new BitSet(), new BitSet(), new BitSet(), new BitSet(), Collections.emptyList(), Collections.emptyList())
                ),
                new ClientboundLevelChunkWithLightPacket(1, 1,
                        new byte[256], new CompoundTag(""), new BlockEntityInfo[] {
                        new BlockEntityInfo(1, 0, 1, BlockEntityType.CHEST, null)
                }, new LightUpdateData(new BitSet(), new BitSet(), new BitSet(), new BitSet(), Collections.emptyList(), Collections.emptyList())
                )
        );
    }
}

package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.data.game.level.LightUpdateData;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityInfo;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.BitSet;
import java.util.Collections;

public class ClientboundLevelChunkWithLightPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(
                new ClientboundLevelChunkWithLightPacket(0, 0,
                        new byte[0], NbtMap.EMPTY, new BlockEntityInfo[0],
                        new LightUpdateData(new BitSet(), new BitSet(), new BitSet(), new BitSet(), Collections.emptyList(), Collections.emptyList())
                ),
                new ClientboundLevelChunkWithLightPacket(1, 1,
                        new byte[256], NbtMap.EMPTY, new BlockEntityInfo[] {
                        new BlockEntityInfo(1, 0, 1, BlockEntityType.CHEST, null)
                }, new LightUpdateData(new BitSet(), new BitSet(), new BitSet(), new BitSet(), Collections.emptyList(), Collections.emptyList())
                )
        );
    }
}

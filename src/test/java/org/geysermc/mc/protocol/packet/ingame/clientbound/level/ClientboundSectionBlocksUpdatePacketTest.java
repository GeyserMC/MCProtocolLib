package org.geysermc.mc.protocol.packet.ingame.clientbound.level;

import org.geysermc.mc.protocol.data.game.level.block.BlockChangeEntry;
import org.geysermc.mc.protocol.packet.PacketTest;
import org.cloudburstmc.math.vector.Vector3i;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundSectionBlocksUpdatePacketTest extends PacketTest {

    @BeforeEach
    public void setup() {
        this.setPackets(
                new ClientboundSectionBlocksUpdatePacket(3, 4, 12, new BlockChangeEntry(Vector3i.from(50, 65, 200), 3))
        );
    }

}

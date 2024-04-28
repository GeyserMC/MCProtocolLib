package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockChangeEntry;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundBlockUpdatePacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(
                new ClientboundBlockUpdatePacket(new BlockChangeEntry(
                        Vector3i.from(1, 61, -1), 3
                ))
        );
    }
}

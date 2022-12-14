package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeEntry;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.cloudburstmc.math.vector.Vector3i;
import org.junit.Before;

public class ClientboundBlockUpdatePacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(
                new ClientboundBlockUpdatePacket(new BlockChangeEntry(
                        Vector3i.from(1, 61, -1), 3
                ))
        );
    }
}

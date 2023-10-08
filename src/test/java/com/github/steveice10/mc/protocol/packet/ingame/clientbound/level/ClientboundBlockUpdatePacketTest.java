package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeEntry;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.cloudburstmc.math.vector.Vector3i;
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

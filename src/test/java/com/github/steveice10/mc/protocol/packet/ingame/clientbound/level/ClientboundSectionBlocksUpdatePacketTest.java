package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeEntry;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.nukkitx.math.vector.Vector3i;
import org.junit.Before;

public class ClientboundSectionBlocksUpdatePacketTest extends PacketTest {

    @Before
    public void setup() {
        this.setPackets(
                new ClientboundSectionBlocksUpdatePacket(3, 4, 12, false, new BlockChangeEntry(Vector3i.from(50, 65, 200), 3))
        );
    }

}

package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeEntry;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ClientboundSectionBlocksUpdatePacketTest extends PacketTest {

    @Before
    public void setup() {
        this.setPackets(
                new ClientboundSectionBlocksUpdatePacket(3, 4, 12, false, new BlockChangeEntry(new Position(50, 65, 200), 3))
        );
    }

}

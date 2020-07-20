package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ServerMultiBlockChangePacketTest extends PacketTest {

    @Before
    public void setup() {
        this.setPackets(
                new ServerMultiBlockChangePacket(1, 1, 1, new BlockChangeRecord(new Position(1, 15, 3), 3))
        );
    }

}

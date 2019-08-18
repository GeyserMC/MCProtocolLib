package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ServerBlockChangePacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(
                new ServerBlockChangePacket(new BlockChangeRecord(
                        new Position(1, 61, -1), new BlockState(3)
                ))
        );
    }
}

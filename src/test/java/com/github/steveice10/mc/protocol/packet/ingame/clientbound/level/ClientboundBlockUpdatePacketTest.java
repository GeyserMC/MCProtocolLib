package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ClientboundBlockUpdatePacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(
                new ClientboundBlockUpdatePacket(new BlockChangeRecord(
                        new Position(1, 61, -1), 3
                ))
        );
    }
}

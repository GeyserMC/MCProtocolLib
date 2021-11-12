package com.github.steveice10.mc.protocol.packet.status.serverbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ServerboundStatusRequestPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new ServerboundStatusRequestPacket());
    }
}

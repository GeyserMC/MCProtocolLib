package com.github.steveice10.mc.protocol.packet.status.server;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class StatusPongPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new StatusPongPacket(System.currentTimeMillis()));
    }
}

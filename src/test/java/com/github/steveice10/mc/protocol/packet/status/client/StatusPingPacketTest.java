package com.github.steveice10.mc.protocol.packet.status.client;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class StatusPingPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new StatusPingPacket(System.currentTimeMillis()));
    }
}

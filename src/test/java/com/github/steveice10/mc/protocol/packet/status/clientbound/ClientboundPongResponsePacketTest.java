package com.github.steveice10.mc.protocol.packet.status.clientbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ClientboundPongResponsePacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new ClientboundPongResponsePacket(System.currentTimeMillis()));
    }
}

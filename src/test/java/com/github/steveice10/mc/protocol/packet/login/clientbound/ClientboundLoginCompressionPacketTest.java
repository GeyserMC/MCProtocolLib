package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ClientboundLoginCompressionPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new ClientboundLoginCompressionPacket(1));
    }
}

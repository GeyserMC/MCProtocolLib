package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class LoginSetCompressionPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new LoginSetCompressionPacket(1));
    }
}

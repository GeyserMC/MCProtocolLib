package com.github.steveice10.mc.protocol.packet.login.client;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class LoginStartPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new LoginStartPacket("Username"));
    }
}

package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

import java.util.Random;

public class LoginPluginRequestPacketTest extends PacketTest {
    @Before
    public void setup() {
        byte[] data = new byte[1024];
        new Random().nextBytes(data);

        this.setPackets(new LoginPluginRequestPacket(0, "Channel", data));
    }
}

package com.github.steveice10.mc.protocol.packet.login.client;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

import java.util.Random;

public class LoginPluginResponsePacketTest extends PacketTest {
    @Before
    public void setup() {
        byte[] data = new byte[1024];
        new Random().nextBytes(data);

        this.setPackets(new LoginPluginResponsePacket(0),
                new LoginPluginResponsePacket(0, data));
    }
}

package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

import java.util.Random;

public class ServerboundCustomQueryPacketTest extends PacketTest {
    @Before
    public void setup() {
        byte[] data = new byte[1024];
        new Random().nextBytes(data);

        this.setPackets(new ServerboundCustomQueryPacket(0),
                new ServerboundCustomQueryPacket(0, data));
    }
}

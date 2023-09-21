package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

import java.util.Random;

public class ServerboundCustomQueryAnswerPacketTest extends PacketTest {
    @Before
    public void setup() {
        byte[] data = new byte[1024];
        new Random().nextBytes(data);

        this.setPackets(new ServerboundCustomQueryAnswerPacket(0),
                new ServerboundCustomQueryAnswerPacket(0, data));
    }
}

package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

import java.util.UUID;

public class ServerboundHelloPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new ServerboundHelloPacket("Username", UUID.randomUUID()));
    }
}

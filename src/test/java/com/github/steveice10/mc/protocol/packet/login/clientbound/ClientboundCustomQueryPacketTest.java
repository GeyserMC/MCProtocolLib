package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;

public class ClientboundCustomQueryPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        byte[] data = new byte[1024];
        new Random().nextBytes(data);

        this.setPackets(new ClientboundCustomQueryPacket(0, "Channel", data));
    }
}

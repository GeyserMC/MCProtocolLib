package com.github.steveice10.mc.protocol.packet.login.serverbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public class ServerboundHelloPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ServerboundHelloPacket("Username", UUID.randomUUID()));
    }
}

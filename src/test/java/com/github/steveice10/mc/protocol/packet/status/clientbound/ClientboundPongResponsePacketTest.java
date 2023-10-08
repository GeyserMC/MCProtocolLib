package com.github.steveice10.mc.protocol.packet.status.clientbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundPongResponsePacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundPongResponsePacket(System.currentTimeMillis()));
    }
}

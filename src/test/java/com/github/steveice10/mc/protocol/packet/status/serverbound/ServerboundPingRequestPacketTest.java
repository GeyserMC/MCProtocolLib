package com.github.steveice10.mc.protocol.packet.status.serverbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ServerboundPingRequestPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ServerboundPingRequestPacket(System.currentTimeMillis()));
    }
}

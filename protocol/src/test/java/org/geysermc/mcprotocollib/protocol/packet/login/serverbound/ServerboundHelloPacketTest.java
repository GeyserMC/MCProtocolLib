package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public class ServerboundHelloPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ServerboundHelloPacket("Username", UUID.randomUUID()));
    }
}

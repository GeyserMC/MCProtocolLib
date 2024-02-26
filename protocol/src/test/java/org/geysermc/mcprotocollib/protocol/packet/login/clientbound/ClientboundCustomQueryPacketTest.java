package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
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

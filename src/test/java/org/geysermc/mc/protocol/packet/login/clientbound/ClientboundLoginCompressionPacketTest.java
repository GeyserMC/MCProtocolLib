package org.geysermc.mc.protocol.packet.login.clientbound;

import org.geysermc.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundLoginCompressionPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundLoginCompressionPacket(1));
    }
}

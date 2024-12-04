package org.geysermc.mcprotocollib.protocol.packet.status.clientbound;

import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.geysermc.mcprotocollib.protocol.packet.ping.clientbound.ClientboundPongResponsePacket;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundPongResponsePacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundPongResponsePacket(System.currentTimeMillis()));
    }
}

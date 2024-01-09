package org.geysermc.mcprotocollib.protocol.packet.status.serverbound;

import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ServerboundPingRequestPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ServerboundPingRequestPacket(System.currentTimeMillis()));
    }
}

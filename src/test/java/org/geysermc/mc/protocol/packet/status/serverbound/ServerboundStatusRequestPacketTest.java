package org.geysermc.mc.protocol.packet.status.serverbound;

import org.geysermc.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ServerboundStatusRequestPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ServerboundStatusRequestPacket());
    }
}

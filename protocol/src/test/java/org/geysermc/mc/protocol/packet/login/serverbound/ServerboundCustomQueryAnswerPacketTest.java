package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;

public class ServerboundCustomQueryAnswerPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        byte[] data = new byte[1024];
        new Random().nextBytes(data);

        this.setPackets(new ServerboundCustomQueryAnswerPacket(0),
                new ServerboundCustomQueryAnswerPacket(0, data));
    }
}

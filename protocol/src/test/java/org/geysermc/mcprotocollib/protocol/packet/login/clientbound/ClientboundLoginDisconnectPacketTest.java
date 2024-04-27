package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundLoginDisconnectPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundLoginDisconnectPacket("Message"),
                new ClientboundLoginDisconnectPacket(Component.text("Message")));
    }
}

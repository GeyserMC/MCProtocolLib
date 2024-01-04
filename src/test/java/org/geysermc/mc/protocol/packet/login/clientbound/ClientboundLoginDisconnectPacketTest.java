package org.geysermc.mc.protocol.packet.login.clientbound;

import org.geysermc.mc.protocol.packet.PacketTest;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundLoginDisconnectPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundLoginDisconnectPacket("Message"),
                new ClientboundLoginDisconnectPacket(Component.text("Message")));
    }
}

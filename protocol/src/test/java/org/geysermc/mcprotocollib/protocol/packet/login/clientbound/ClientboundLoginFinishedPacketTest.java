package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public class ClientboundLoginFinishedPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundLoginFinishedPacket(new GameProfile(UUID.randomUUID(), "Username")));
    }
}

package org.geysermc.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import org.geysermc.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public class ClientboundGameProfilePacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundGameProfilePacket(new GameProfile(UUID.randomUUID(), "Username")));
    }
}

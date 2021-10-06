package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

import java.util.UUID;

public class ClientboundGameProfilePacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new ClientboundGameProfilePacket(new GameProfile(UUID.randomUUID(), "Username")));
    }
}

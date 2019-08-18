package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

import java.util.UUID;

public class LoginSuccessPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new LoginSuccessPacket(new GameProfile(UUID.randomUUID(), "Username")));
    }
}

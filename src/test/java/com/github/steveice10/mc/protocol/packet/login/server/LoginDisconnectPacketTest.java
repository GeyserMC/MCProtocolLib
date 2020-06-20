package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class LoginDisconnectPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new LoginDisconnectPacket("Message"),
                new LoginDisconnectPacket(new TextMessage.Builder().text("Message").build()));
    }
}

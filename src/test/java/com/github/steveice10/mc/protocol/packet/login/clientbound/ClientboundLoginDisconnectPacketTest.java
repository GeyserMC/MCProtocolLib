package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import net.kyori.adventure.text.Component;
import org.junit.Before;

public class ClientboundLoginDisconnectPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new ClientboundLoginDisconnectPacket("Message"),
                new ClientboundLoginDisconnectPacket(Component.text("Message")));
    }
}

package com.github.steveice10.mc.protocol.packet.handshake.client;

import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.packetlib.packet.Packet;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class HandshakePacketTest extends PacketTest {
    @Before
    public void setup() {
        List<Packet> packets = new ArrayList<>();
        for (HandshakeIntent intent : HandshakeIntent.values()) {
            packets.add(new HandshakePacket(MinecraftConstants.PROTOCOL_VERSION, "localhost", 25565, intent));
        }

        this.setPackets(packets.toArray(new Packet[0]));
    }
}

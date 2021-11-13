package com.github.steveice10.mc.protocol.packet.handshake.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodec;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.packetlib.packet.Packet;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class ClientIntentionPacketTest extends PacketTest {
    @Before
    public void setup() {
        List<Packet> packets = new ArrayList<>();
        for (HandshakeIntent intent : HandshakeIntent.values()) {
            packets.add(new ClientIntentionPacket(MinecraftCodec.CODEC.getProtocolVersion(), "localhost", 25565, intent));
        }

        this.setPackets(packets.toArray(new Packet[0]));
    }
}

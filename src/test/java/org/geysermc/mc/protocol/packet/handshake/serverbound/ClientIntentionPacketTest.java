package org.geysermc.mc.protocol.packet.handshake.serverbound;

import org.geysermc.mc.protocol.codec.MinecraftCodec;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import org.geysermc.mc.protocol.data.handshake.HandshakeIntent;
import org.geysermc.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public class ClientIntentionPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        List<MinecraftPacket> packets = new ArrayList<>();
        for (HandshakeIntent intent : HandshakeIntent.values()) {
            packets.add(new ClientIntentionPacket(MinecraftCodec.CODEC.getProtocolVersion(), "localhost", 25565, intent));
        }

        this.setPackets(packets.toArray(new MinecraftPacket[0]));
    }
}

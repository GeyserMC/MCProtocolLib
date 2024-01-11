package org.geysermc.mcprotocollib.protocol.packet.handshake.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodec;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.handshake.HandshakeIntent;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
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

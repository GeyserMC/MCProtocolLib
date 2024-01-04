package org.geysermc.mc.protocol.packet.status.clientbound;

import org.geysermc.mc.auth.data.GameProfile;
import org.geysermc.mc.protocol.codec.MinecraftCodec;
import org.geysermc.mc.protocol.data.status.PlayerInfo;
import org.geysermc.mc.protocol.data.status.ServerStatusInfo;
import org.geysermc.mc.protocol.data.status.VersionInfo;
import org.geysermc.mc.protocol.packet.PacketTest;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class ClientboundStatusResponsePacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundStatusResponsePacket(
                new ServerStatusInfo(
                        new VersionInfo(MinecraftCodec.CODEC.getMinecraftVersion(), MinecraftCodec.CODEC.getProtocolVersion()),
                        new PlayerInfo(100, 10, new ArrayList<>(
                                Collections.singleton(new GameProfile(UUID.randomUUID(), "Username"))
                        )),
                        Component.text("Description"),
                        null,
                        false
                )
        ));
    }
}

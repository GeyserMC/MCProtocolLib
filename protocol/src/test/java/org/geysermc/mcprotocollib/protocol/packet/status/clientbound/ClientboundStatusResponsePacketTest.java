package org.geysermc.mcprotocollib.protocol.packet.status.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodec;
import org.geysermc.mcprotocollib.protocol.data.status.PlayerInfo;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;
import org.geysermc.mcprotocollib.protocol.data.status.VersionInfo;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
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

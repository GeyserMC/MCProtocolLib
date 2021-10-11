package com.github.steveice10.mc.protocol.packet.status.server;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import net.kyori.adventure.text.Component;
import org.junit.Before;

import java.util.UUID;

public class StatusResponsePacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new StatusResponsePacket(
                new ServerStatusInfo(
                        VersionInfo.CURRENT,
                        new PlayerInfo(100, 10, new GameProfile[]{
                                new GameProfile(UUID.randomUUID(), "Username")
                        }),
                        Component.text("Description"),
                        null
                )
        ));
    }
}

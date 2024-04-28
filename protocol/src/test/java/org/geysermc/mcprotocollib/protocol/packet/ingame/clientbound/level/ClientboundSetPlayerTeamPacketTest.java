package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.CollisionRule;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.NameTagVisibility;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.TeamColor;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetPlayerTeamPacket;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundSetPlayerTeamPacketTest extends PacketTest {

    @BeforeEach
    public void setup() {
        // Test nameTagVisibility and collisionRule encoding/decoding
        this.setPackets(
                new ClientboundSetPlayerTeamPacket("dummy", Component.empty(), Component.empty(), Component.empty(),
                        true, false, NameTagVisibility.NEVER, CollisionRule.PUSH_OWN_TEAM, TeamColor.RESET),
                new ClientboundSetPlayerTeamPacket("dummy", Component.empty(), Component.empty(), Component.empty(),
                        false, true, NameTagVisibility.HIDE_FOR_OTHER_TEAMS, CollisionRule.PUSH_OTHER_TEAMS, TeamColor.RED)
        );
    }
}

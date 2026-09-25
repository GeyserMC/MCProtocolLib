package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.ContainerType;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundOpenScreenPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(
                // Vanilla menu type: type is resolved and the raw id round-trips.
                new ClientboundOpenScreenPacket(1, ContainerType.GENERIC_9X1, ContainerType.GENERIC_9X1.ordinal(), Component.text("Test")),
                // Modded menu type id (outside the vanilla range): type is null,
                // but the raw id is preserved so re-encoding does not NPE and
                // logging callers can still report the exact id.
                new ClientboundOpenScreenPacket(2, null, 100, Component.text("Modded"))
        );
    }
}

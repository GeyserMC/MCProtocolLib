package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEventType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.UnknownEntityEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientboundEntityEventPacketTest {

    @ParameterizedTest
    @CsvSource({
            "2, KINETIC_HIT",
            "70, TNT_PRIME",
            "71, BOAT_SINK",
            "72, BOAT_LAUNCH"
    })
    public void decodesVanillaEntityEventIds(int eventId, EntityEventType expected) {
        ClientboundEntityEventPacket packet = new ClientboundEntityEventPacket(eventBytes(42, eventId));

        assertEquals(42, packet.getEntityId());
        assertEquals(expected, packet.getEvent());
    }

    @Test
    public void keepsUnknownEntityEventIdsWhenForwarding() {
        ByteBuf in = eventBytes(42, 29);
        byte[] original = ByteBufUtil.getBytes(in);

        ClientboundEntityEventPacket packet = new ClientboundEntityEventPacket(in);
        ByteBuf out = Unpooled.buffer();
        packet.serialize(out);

        assertEquals(new UnknownEntityEvent(29), packet.getEvent());
        assertEquals(ByteBufUtil.hexDump(original), ByteBufUtil.hexDump(out));
    }

    private static ByteBuf eventBytes(int entityId, int eventId) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(entityId);
        buf.writeByte(eventId);
        return buf;
    }
}

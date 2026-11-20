package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.PotentSulfurValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.PotentSulfurValueType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ClientboundBlockEventPacketTest {

    @Test
    public void decodesPotentSulfurEruptionStart() {
        ByteBuf in = Unpooled.buffer();
        MinecraftTypes.writePosition(in, Vector3i.from(1, 64, -2));
        in.writeByte(0);
        in.writeByte(0);
        MinecraftTypes.writeVarInt(in, 1089); // minecraft:potent_sulfur

        ClientboundBlockEventPacket packet = new ClientboundBlockEventPacket(in);

        assertEquals(PotentSulfurValueType.START_ERUPTION, packet.getType());
        assertInstanceOf(PotentSulfurValue.class, packet.getValue());
    }
}

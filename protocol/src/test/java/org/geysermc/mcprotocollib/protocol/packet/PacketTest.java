package org.geysermc.mcprotocollib.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class PacketTest {
    private MinecraftPacket[] packets;

    protected void setPackets(MinecraftPacket... packets) {
        this.packets = packets;
    }

    @Test
    public void testPackets() {
        for (MinecraftPacket packet : this.packets) {
            ByteBuf buf = Unpooled.buffer();
            packet.serialize(buf);

            Packet decoded = this.createPacket(packet.getClass(), buf);

            assertEquals(packet, decoded, "Decoded packet does not match original: " + packet + " vs " + decoded);
        }
    }

    private Packet createPacket(Class<? extends Packet> clazz, ByteBuf in) {
        try {
            Constructor<? extends Packet> constructor = clazz.getConstructor(ByteBuf.class);

            return constructor.newInstance(in);
        } catch (NoSuchMethodError e) {
            throw new IllegalStateException("Packet \"" + clazz.getName() + "\" does not have a NetInput constructor for instantiation.");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + clazz.getName() + "\".", e);
        }
    }
}

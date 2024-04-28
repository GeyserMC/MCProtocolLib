package org.geysermc.mcprotocollib.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class PacketTest {
    private MinecraftPacket[] packets;

    protected void setPackets(MinecraftPacket... packets) {
        this.packets = packets;
    }

    @Test
    public void testPackets() throws Exception {
        MinecraftCodecHelper helper = new MinecraftCodecHelper(Int2ObjectMaps.emptyMap(), Collections.emptyMap());
        for (MinecraftPacket packet : this.packets) {
            ByteBuf buf = Unpooled.buffer();
            packet.serialize(buf, helper);

            Packet decoded = this.createPacket(packet.getClass(), helper, buf);

            assertEquals(packet, decoded, "Decoded packet does not match original: " + packet + " vs " + decoded);
        }
    }

    private Packet createPacket(Class<? extends Packet> clazz, MinecraftCodecHelper helper, ByteBuf in) {
        try {
            Constructor<? extends Packet> constructor = clazz.getConstructor(ByteBuf.class, MinecraftCodecHelper.class);

            return constructor.newInstance(in, helper);
        } catch (NoSuchMethodError e) {
            throw new IllegalStateException("Packet \"" + clazz.getName() + "\" does not have a NetInput constructor for instantiation.");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + clazz.getName() + "\".", e);
        }
    }
}

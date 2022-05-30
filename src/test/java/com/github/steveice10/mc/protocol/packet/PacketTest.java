package com.github.steveice10.mc.protocol.packet;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.event.LevelEvent;
import com.github.steveice10.packetlib.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;

public abstract class PacketTest {
    private MinecraftPacket[] packets;

    protected void setPackets(MinecraftPacket... packets) {
        this.packets = packets;
    }

    @Test
    public void testPackets() throws Exception {
        MinecraftCodecHelper helper = new MinecraftCodecHelper(TypeMap.empty(LevelEvent.class));
        for (MinecraftPacket packet : this.packets) {
            ByteBuf buf = Unpooled.buffer();
            packet.serialize(buf, helper);

            Packet decoded = this.createPacket(packet.getClass(), helper, buf);

            assertEquals("Decoded packet does not match original: " + packet + " vs " + decoded, packet, decoded);
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

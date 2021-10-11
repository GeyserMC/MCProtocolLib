package com.github.steveice10.mc.protocol.packet;

import com.github.steveice10.packetlib.io.stream.StreamNetInput;
import com.github.steveice10.packetlib.io.stream.StreamNetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;

public abstract class PacketTest {
    private Packet[] packets;

    protected void setPackets(Packet... packets) {
        this.packets = packets;
    }

    @Test
    public void testPackets() throws Exception {
        for (Packet packet : this.packets) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            packet.write(new StreamNetOutput(out));
            byte[] encoded = out.toByteArray();

            Packet decoded = this.createPacket(packet.getClass());
            decoded.read(new StreamNetInput(new ByteArrayInputStream(encoded)));

            assertEquals("Decoded packet does not match original: " + packet + " vs " + decoded, packet, decoded);
        }
    }

    private Packet createPacket(Class<? extends Packet> clazz) {
        try {
            Constructor<? extends Packet> constructor = clazz.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        } catch (NoSuchMethodError e) {
            throw new IllegalStateException("Packet \"" + clazz.getName() + "\" does not have a no-params constructor for instantiation.");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + clazz.getName() + "\".", e);
        }
    }
}

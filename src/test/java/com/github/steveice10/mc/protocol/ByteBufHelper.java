package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.io.ByteBufNetInput;
import com.github.steveice10.packetlib.tcp.io.ByteBufNetOutput;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ByteBufHelper {
    private static final ByteBuf buffer = Unpooled.buffer();

    public static final ByteBufNetOutput out = new ByteBufNetOutput(buffer);
    public static final ByteBufNetInput in = new ByteBufNetInput(buffer);

    @SuppressWarnings("unchecked")
    public static <T> T writeAndRead(Packet writable) {
        try {
            buffer.clear();

            writable.write(out);

            // Creating new fresh packet to reset fields.
            Constructor constructor = writable.getClass().getDeclaredConstructor();
            constructor.setAccessible(true);

            Packet readable = (Packet) constructor.newInstance();
            readable.read(in);

            assertFalse("Buffer is not empty", buffer.isReadable());

            return (T) readable;
        } catch(Exception e) {
            throw new IllegalStateException("Failed parse packet", e);
        }
    }

    public static void assertPosition(Position position, int x, int y, int z) {
        assertEquals("Received incorrect X position", x, position.getX());
        assertEquals("Received incorrect Y position", y, position.getY());
        assertEquals("Received incorrect Z position", z, position.getZ());
    }

    public static void assertBlock(BlockChangeRecord record, int block, int data) {
        assertEquals("Received incorrect block id", block, record.getBlock().getId());
        assertEquals("Received incorrect block data", data, record.getBlock().getData());
    }
}

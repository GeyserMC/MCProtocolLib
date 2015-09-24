package org.spacehq.mc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.world.block.BlockChangeRecord;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.tcp.io.ByteBufNetInput;
import org.spacehq.packetlib.tcp.io.ByteBufNetOutput;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ByteBufHelper {

    private static final ByteBuf buffer = Unpooled.buffer();

    protected static final ByteBufNetOutput out = new ByteBufNetOutput(buffer);
    protected static final ByteBufNetInput in = new ByteBufNetInput(buffer);

    @SuppressWarnings("unchecked")
    public static <T> T writeAndRead(Packet writable){
        if(buffer.isReadable()) {
            buffer.release();
        }

        try {
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
        } finally {
            buffer.release();
        }
    }

    public static void assertPosition(Position position, int x, int y, int z) {
        assertEquals("Received incorrect X position", x, position.getX());
        assertEquals("Received incorrect Y position", y, position.getY());
        assertEquals("Received incorrect Z position", z, position.getZ());
    }

    public static void assertBlock(BlockChangeRecord record, int block, int data) {
        assertEquals("Received incorrect block id", block, record.getId());
        assertEquals("Received incorrect block data", data, record.getData());
    }

}

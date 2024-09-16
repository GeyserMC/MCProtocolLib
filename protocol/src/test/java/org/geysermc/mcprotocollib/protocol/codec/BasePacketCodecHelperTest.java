package org.geysermc.mcprotocollib.protocol.codec;

import io.netty.buffer.Unpooled;
import org.geysermc.mcprotocollib.network.codec.BasePacketCodecHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasePacketCodecHelperTest {
    private static final BasePacketCodecHelper codecHelper = new BasePacketCodecHelper();

    @Test
    public void readVarInt() {
        assertEquals(0x80808080, codecHelper.readVarInt(Unpooled.wrappedBuffer(new byte[]{
            (byte) 0x80, (byte) 0x81, (byte) 0x82, (byte) 0x84, (byte) 0x08
        })));
    }

    @Test
    public void readVarIntTooLong() {
        try {
            // VarInt too long error should take precedence over IndexOutOfBoundsException
            codecHelper.readVarInt(Unpooled.wrappedBuffer(new byte[]{
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80
            }));
        } catch (RuntimeException ex) {
            // (assertThrow doesn't work as IndexOutOfBoundsException is also a RuntimeException)
            assertEquals("VarInt wider than 5 bytes", ex.getMessage());
        }
    }

    @Test
    public void readVarLong() {
        assertEquals(0x8080808080808080L, codecHelper.readVarLong(Unpooled.wrappedBuffer(new byte[]{
            (byte) 0x80, (byte) 0x81, (byte) 0x82, (byte) 0x84, (byte) 0x88, (byte) 0x90, (byte) 0xa0, (byte) 0xc0, (byte) 0x80, (byte) 0x01
        })));
    }

    @Test
    public void readVarLongTooLong() {
        try {
            // VarLong too long error should take precedence over IndexOutOfBoundsException
            codecHelper.readVarLong(Unpooled.wrappedBuffer(new byte[]{
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80
            }));
        } catch (RuntimeException ex) {
            assertEquals("VarLong wider than 10 bytes", ex.getMessage());
        }
    }
}

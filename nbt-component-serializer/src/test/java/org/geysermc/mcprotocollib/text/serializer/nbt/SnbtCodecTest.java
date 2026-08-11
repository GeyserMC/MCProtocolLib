package org.geysermc.mcprotocollib.text.serializer.nbt;

import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SnbtCodecTest {

    @Test
    public void testWritesNumberSuffixes() {
        assertEquals("1b", SnbtCodec.toSnbt((byte) 1));
        assertEquals("1s", SnbtCodec.toSnbt((short) 1));
        assertEquals("1", SnbtCodec.toSnbt(1));
        assertEquals("1L", SnbtCodec.toSnbt(1L));
        assertEquals("1.5f", SnbtCodec.toSnbt(1.5F));
        assertEquals("1.5d", SnbtCodec.toSnbt(1.5D));
        assertEquals("-3b", SnbtCodec.toSnbt((byte) -3));
    }

    @Test
    public void testReadsNumberSuffixes() {
        assertEquals((byte) 1, SnbtCodec.fromSnbt("1b"));
        assertEquals((byte) 1, SnbtCodec.fromSnbt("1B"));
        assertEquals((short) 1, SnbtCodec.fromSnbt("1s"));
        assertEquals(1, SnbtCodec.fromSnbt("1"));
        assertEquals(1L, SnbtCodec.fromSnbt("1l"));
        assertEquals(1L, SnbtCodec.fromSnbt("1L"));
        assertEquals(1.5F, SnbtCodec.fromSnbt("1.5f"));
        assertEquals(1.5D, SnbtCodec.fromSnbt("1.5d"));
        assertEquals(-1.5D, SnbtCodec.fromSnbt("-1.5"));
        assertEquals(1.0E10D, SnbtCodec.fromSnbt("1.0E10"));
    }

    @Test
    public void testReadsBooleansAsBytes() {
        assertEquals((byte) 1, SnbtCodec.fromSnbt("true"));
        assertEquals((byte) 0, SnbtCodec.fromSnbt("false"));
    }

    @Test
    public void testArrays() {
        assertEquals("[B;1b,-2b]", SnbtCodec.toSnbt(new byte[]{1, -2}));
        assertEquals("[I;1,-2]", SnbtCodec.toSnbt(new int[]{1, -2}));
        assertEquals("[L;1L,-2L]", SnbtCodec.toSnbt(new long[]{1, -2}));
        assertEquals("[B;]", SnbtCodec.toSnbt(new byte[0]));

        assertArrayEquals(new byte[]{1, -2}, (byte[]) SnbtCodec.fromSnbt("[B;1b,-2b]"));
        assertArrayEquals(new int[]{1, -2}, (int[]) SnbtCodec.fromSnbt("[I;1,-2]"));
        assertArrayEquals(new long[]{1, -2}, (long[]) SnbtCodec.fromSnbt("[L;1L,-2L]"));
        assertArrayEquals(new int[0], (int[]) SnbtCodec.fromSnbt("[I;]"));
    }

    @Test
    public void testEmptyCompoundAndList() {
        assertEquals("{}", SnbtCodec.toSnbt(NbtMap.EMPTY));
        assertEquals("[]", SnbtCodec.toSnbt(new NbtList<>(NbtType.END, List.of())));

        assertEquals(NbtMap.EMPTY, SnbtCodec.fromSnbt("{}"));
        assertEquals(new NbtList<>(NbtType.END, List.of()), SnbtCodec.fromSnbt("[]"));
    }

    @Test
    public void testNesting() {
        final NbtMap tag = NbtMap.builder()
            .putCompound("inner", NbtMap.builder().putString("text", "hi").build())
            .putList("values", NbtType.INT, List.of(1, 2, 3))
            .build();

        assertEquals("{inner:{text:\"hi\"},values:[1,2,3]}", SnbtCodec.toSnbt(tag));
        assertEquals(tag, SnbtCodec.fromSnbt("{inner:{text:\"hi\"},values:[1,2,3]}"));
    }

    @Test
    public void testListOfCompounds() {
        final NbtList<NbtMap> list = new NbtList<>(NbtType.COMPOUND, List.of(
            NbtMap.builder().putString("a", "1").build(),
            NbtMap.builder().putString("a", "2").build()
        ));

        assertEquals("[{a:\"1\"},{a:\"2\"}]", SnbtCodec.toSnbt(list));
        assertEquals(list, SnbtCodec.fromSnbt("[{a:\"1\"},{a:\"2\"}]"));
    }

    @Test
    public void testQuotingAndEscaping() {
        final NbtMap tag = NbtMap.builder()
            .putString("plain-key.1+2_3", "value")
            .putString("key with space", "a \"quoted\" \\ backslash")
            .build();

        final String snbt = SnbtCodec.toSnbt(tag);
        assertEquals("{plain-key.1+2_3:\"value\",\"key with space\":\"a \\\"quoted\\\" \\\\ backslash\"}", snbt);
        assertEquals(tag, SnbtCodec.fromSnbt(snbt));
    }

    @Test
    public void testStringsThatLookLikeNumbersStayStrings() {
        assertEquals("{a:\"1\"}", SnbtCodec.toSnbt(NbtMap.builder().putString("a", "1").build()));
        assertEquals("1", SnbtCodec.fromSnbt("\"1\""));
    }

    @Test
    public void testReadsUnquotedAndSingleQuotedStrings() {
        assertEquals("hello", SnbtCodec.fromSnbt("hello"));
        assertEquals("hello", SnbtCodec.fromSnbt("'hello'"));
        assertEquals("a\"b", SnbtCodec.fromSnbt("'a\"b'"));
        assertEquals("a'b", SnbtCodec.fromSnbt("\"a'b\""));
        assertEquals("a'b", SnbtCodec.fromSnbt("'a\\'b'"));

        assertEquals(NbtMap.builder().putString("key", "value").build(), SnbtCodec.fromSnbt("{key:value}"));
        assertEquals(NbtMap.builder().putString("a b", "c").build(), SnbtCodec.fromSnbt("{'a b':'c'}"));
    }

    @Test
    public void testWhitespaceTolerance() {
        final NbtMap expected = NbtMap.builder()
            .putInt("a", 1)
            .putList("b", NbtType.STRING, List.of("x", "y"))
            .build();

        assertEquals(expected, SnbtCodec.fromSnbt("  { a : 1 , b : [ \"x\" , \"y\" ] }  "));
        assertArrayEquals(new int[]{1, 2}, (int[]) SnbtCodec.fromSnbt("[I; 1 , 2 ]"));
    }

    @Test
    public void testRoundTrip() {
        final NbtMap fixture = NbtMap.builder()
            .putByte("byte", (byte) -128)
            .putShort("short", (short) 32767)
            .putInt("int", Integer.MIN_VALUE)
            .putLong("long", Long.MAX_VALUE)
            .putFloat("float", 3.5F)
            .putDouble("double", -12.75D)
            .putString("string", "quote \" backslash \\ done")
            .putString("empty", "")
            .putByteArray("bytes", new byte[]{0, 1, -1})
            .putIntArray("ints", new int[]{0, 1, -1})
            .putLongArray("longs", new long[]{0, 1, -1})
            .putCompound("emptyCompound", NbtMap.EMPTY)
            .putList("emptyList", NbtType.END, List.of())
            .putList("strings", NbtType.STRING, List.of("a", "b"))
            .putList("compounds", NbtType.COMPOUND, List.of(
                NbtMap.builder().putInt("depth", 1).putCompound("nested",
                    NbtMap.builder().putList("doubles", NbtType.DOUBLE, List.of(1.0D, 2.5D)).build()).build()
            ))
            .putList("lists", NbtType.LIST, List.of(new NbtList<>(NbtType.INT, List.of(1, 2))))
            .build();

        assertEquals(fixture, SnbtCodec.fromSnbt(SnbtCodec.toSnbt(fixture)));
    }

    @Test
    public void testRejectsMixedLists() {
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("[1b,\"a\"]"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("[1,2L]"));
    }

    @Test
    public void testRejectsMalformedInput() {
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt(""));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("{"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("{a:1"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("{a}"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("{:1}"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("[1,2"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("\"unterminated"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("\"bad \\escape\""));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("{a:1}}"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("[B;\"a\"]"));
        assertThrows(NbtSerializationException.class, () -> SnbtCodec.fromSnbt("999999999999999999999999b"));
    }
}

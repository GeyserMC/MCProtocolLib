package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.text.format.ShadowColor;
import org.cloudburstmc.nbt.NbtList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class NbtUtilTest {

    @Test
    void testDeserializeLenientUUID() {
        Assertions.assertEquals(UUID.fromString("67b956e4-ba17-41fd-93f7-1c9feb94f6b4"), NbtUtil.deserializeLenientUUID("67b956e4-ba17-41fd-93f7-1c9feb94f6b4"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> NbtUtil.deserializeLenientUUID(12345));
    }

    @Test
    void testDeserializeInvalidUUIDArray() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> NbtUtil.deserializeUUID(new int[0]));
    }

    @Test
    void testDeserializeARGB() {
        Assertions.assertEquals(ShadowColor.shadowColor(51, 102, 153, 204), NbtUtil.deserializeARGB(HeterogeneousNbtList.of(0.2F, 0.4F, 0.6F, 0.8F)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> NbtUtil.deserializeARGB(NbtList.EMPTY));
        Assertions.assertThrows(IllegalArgumentException.class, () -> NbtUtil.deserializeARGB(HeterogeneousNbtList.of(0.2, 0.4, 0.6, 0.8))); // not floats
        Assertions.assertThrows(IllegalArgumentException.class, () -> NbtUtil.deserializeARGB(HeterogeneousNbtList.of(0.2F, 0.4F)));

        Assertions.assertEquals(ShadowColor.shadowColor(0x21325678), NbtUtil.deserializeARGB(0x21325678));
        Assertions.assertThrows(IllegalArgumentException.class, () -> NbtUtil.deserializeARGB((byte) 0));
    }
}

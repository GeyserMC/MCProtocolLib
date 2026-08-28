package org.geysermc.adventure.text.serializer.nbt;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NbtBinaryTagHolderTest {

    @Test
    void testCodec() {
        NbtMap testTag = NbtMap.builder()
            .putString("hello", "eclipse here")
            .putBoolean("cool", false)
            .putInt("songs_listened_to", 500)
            .putList("likes", NbtType.STRING, "shell", "trains")
            .build();

        Assertions.assertEquals(testTag, NbtBinaryTagHolder.NBT_CODEC.decode(NbtBinaryTagHolder.NBT_CODEC.encode(testTag)));
    }

    @Test
    void testBinaryTagEncode() {
        NbtBinaryTagHolder dataComponent = new NbtBinaryTagHolder(NbtMap.builder()
            .putFloat("min_reach", 3.0F)
            .putFloat("max_creative_reach", 6.0F)
            .build());

        Assertions.assertEquals(dataComponent.tag(), dataComponent.get(NbtBinaryTagHolder.NBT_CODEC));
    }
}

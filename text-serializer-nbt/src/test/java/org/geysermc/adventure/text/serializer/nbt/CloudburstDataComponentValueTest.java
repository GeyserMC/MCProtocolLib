package org.geysermc.adventure.text.serializer.nbt;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CloudburstDataComponentValueTest {

    @Test
    void testCodec() {
        NbtMap testTag = NbtMap.builder()
            .putString("hello", "eclipse here")
            .putBoolean("cool", false)
            .putInt("songs_listened_to", 500)
            .putList("likes", NbtType.STRING, "shell", "trains")
            .build();

        Assertions.assertEquals(testTag, CloudburstDataComponentValue.NBT_CODEC.decode(CloudburstDataComponentValue.NBT_CODEC.encode(testTag)));
    }

    @Test
    void testBinaryTagEncode() {
        CloudburstDataComponentValue dataComponent = new CloudburstDataComponentValue(NbtMap.builder()
            .putFloat("min_reach", 3.0F)
            .putFloat("max_creative_reach", 6.0F)
            .build());

        Assertions.assertEquals(dataComponent.value(), dataComponent.asBinaryTag().get(CloudburstDataComponentValue.NBT_CODEC));
    }
}

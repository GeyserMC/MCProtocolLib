package org.geysermc.mcprotocollib.protocol.codec;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.cloudburstmc.nbt.NbtMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NbtComponentSerializerTest {

    @Test
    public void hatFlagSerializesAsBoolean() {
        JsonObject json = NbtComponentSerializer.tagComponentToJson(playerHeadComponent((byte) 1)).getAsJsonObject();

        assertTrue(json.get("hat").isJsonPrimitive());
        assertTrue(json.get("hat").getAsJsonPrimitive().isBoolean());
        assertTrue(json.get("hat").getAsBoolean());
    }

    @Test
    public void readComponentAcceptsHatFlag() {
        ByteBuf buf = Unpooled.buffer();
        try {
            MinecraftTypes.writeAnyTag(buf, playerHeadComponent((byte) 0));

            // Deserialization is strict about boolean fields; a hat flag crossing
            // the NBT to JSON conversion as a number fails here.
            ObjectComponent component = assertInstanceOf(ObjectComponent.class, MinecraftTypes.readComponent(buf));
            PlayerHeadObjectContents contents = assertInstanceOf(PlayerHeadObjectContents.class, component.contents());
            assertFalse(contents.hat());
        } finally {
            buf.release();
        }
    }

    private static NbtMap playerHeadComponent(byte hat) {
        return NbtMap.builder()
                .putString("type", "object")
                .putString("player", "0123456789abcdef0123456789abcdef")
                .putByte("hat", hat)
                .build();
    }
}

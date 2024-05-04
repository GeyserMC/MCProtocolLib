package org.geysermc.mcprotocollib.protocol.data;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Miscellaneous tests for reading and writing classes to/from the network
 */
public class NetworkDataTests {

    @Test
    public void testEffects() {
        for (Effect effect : Effect.VALUES) {
            MinecraftByteBuf buf = new MinecraftByteBuf(Int2ObjectMaps.emptyMap(), Collections.emptyMap(), Unpooled.buffer());
            buf.writeEffect(effect);

            assertEquals(effect, buf.readEffect());
        }
    }
}

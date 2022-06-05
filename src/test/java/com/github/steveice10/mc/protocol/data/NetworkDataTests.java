package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Miscellaneous tests for reading and writing classes to/from the network
 */
public class NetworkDataTests {

    @Test
    public void testEffects() {
        MinecraftCodecHelper helper = new MinecraftCodecHelper(Int2ObjectMaps.emptyMap(), Collections.emptyMap());
        for (Effect effect : Effect.VALUES) {
            ByteBuf buf = Unpooled.buffer();
            helper.writeEffect(buf, effect);

            Assert.assertEquals(effect, helper.readEffect(buf));
        }
    }
}

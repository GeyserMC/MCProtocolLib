package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.mc.protocol.data.game.level.event.LevelEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.junit.Assert;
import org.junit.Test;

/**
 * Miscellaneous tests for reading and writing classes to/from the network
 */
public class NetworkDataTests {

    @Test
    public void testEffects() {
        MinecraftCodecHelper helper = new MinecraftCodecHelper(TypeMap.empty(LevelEvent.class));
        for (Effect effect : Effect.VALUES) {
            ByteBuf buf = Unpooled.buffer();
            helper.writeEffect(buf, effect);

            Assert.assertEquals(effect, helper.readEffect(buf));
        }
    }
}

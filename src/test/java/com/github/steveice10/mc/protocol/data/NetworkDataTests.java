package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import org.junit.Assert;
import org.junit.Test;

/**
 * Miscellaneous tests for reading and writing classes to/from the network
 */
public class NetworkDataTests {

    @Test
    public void testEffects() {
        for (Effect effect : Effect.VALUES) {
            int networkId = Effect.toNetworkId(effect);
            Assert.assertEquals(effect, Effect.fromNetworkId(networkId));
        }
    }
}

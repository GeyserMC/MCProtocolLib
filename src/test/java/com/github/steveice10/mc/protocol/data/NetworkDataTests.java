package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityType;
import com.github.steveice10.packetlib.io.stream.StreamNetInput;
import com.github.steveice10.packetlib.io.stream.StreamNetOutput;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Miscellaneous tests for reading and writing classes to/from the network
 */
public class NetworkDataTests {

    @Test
    public void testBlockEntities() throws IOException {
        for (BlockEntityType type : BlockEntityType.values()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            StreamNetOutput out = new StreamNetOutput(stream);
            BlockEntityType.write(out, type);
            StreamNetInput in = new StreamNetInput(new ByteArrayInputStream(stream.toByteArray()));

            Assert.assertEquals(type, BlockEntityType.read(in));
        }
    }

    @Test
    public void testEffects() {
        for (Effect effect : Effect.VALUES) {
            int networkId = Effect.toNetworkId(effect);
            Assert.assertEquals(effect, Effect.fromNetworkId(networkId));
        }
    }
}

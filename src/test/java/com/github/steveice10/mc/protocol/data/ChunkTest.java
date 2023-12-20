package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.chunk.ChunkSection;
import com.github.steveice10.mc.protocol.data.game.chunk.DataPalette;
import com.github.steveice10.mc.protocol.data.game.chunk.palette.PaletteType;
import com.github.steveice10.mc.protocol.data.game.chunk.palette.SingletonPalette;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ChunkTest {
    private final List<ChunkSection> chunkSectionsToTest = new ArrayList<>();

    @BeforeEach
    public void setup() {
        chunkSectionsToTest.add(new ChunkSection());

        ChunkSection section = new ChunkSection();
        section.setBlock(0, 0, 0, 10);
        chunkSectionsToTest.add(section);

        SingletonPalette singletonPalette = new SingletonPalette(20);
        DataPalette dataPalette = new DataPalette(singletonPalette, null, PaletteType.CHUNK);
        DataPalette biomePalette = new DataPalette(singletonPalette, null, PaletteType.BIOME);
        section = new ChunkSection(4096, dataPalette, biomePalette);
        chunkSectionsToTest.add(section);
    }

    @Test
    public void testChunkSectionEncoding() throws IOException {
        MinecraftCodecHelper helper = new MinecraftCodecHelper(Int2ObjectMaps.emptyMap(), Collections.emptyMap());
        for (ChunkSection section : chunkSectionsToTest) {
            ByteBuf buf = Unpooled.buffer();
            helper.writeChunkSection(buf, section);
            ChunkSection decoded;
            try {
                decoded = helper.readChunkSection(buf);
            } catch (Exception e) {
                System.out.println(section);
                e.printStackTrace();
                throw e;
            }

            assertEquals(section, decoded, "Decoded packet does not match original: " + section + " vs " + decoded);
        }
    }

    @Test
    public void testDeepCopy() {
        for (ChunkSection section : chunkSectionsToTest) {
            ChunkSection copy = new ChunkSection(section);
            assertEquals(section, copy, "Deep copy does not match original: " + section + " vs " + copy);

            copy.setBlock(1, 1, 1, 10);
            assertNotEquals(section, copy, "Deep copy is not deep: " + section + " vs " + copy);
        }
    }
}

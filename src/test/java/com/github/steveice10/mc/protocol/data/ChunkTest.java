package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.chunk.ChunkSection;
import com.github.steveice10.mc.protocol.data.game.chunk.DataPalette;
import com.github.steveice10.mc.protocol.data.game.chunk.palette.PaletteType;
import com.github.steveice10.mc.protocol.data.game.chunk.palette.SingletonPalette;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChunkTest {
    private final List<ChunkSection> chunkSectionsToTest = new ArrayList<>();

    @Before
    public void setup() {
        chunkSectionsToTest.add(new ChunkSection());

        ChunkSection section = new ChunkSection();
        section.setBlock(0, 0, 0, 10);
        chunkSectionsToTest.add(section);

        SingletonPalette singletonPalette = new SingletonPalette(20);
        DataPalette dataPalette = new DataPalette(singletonPalette, null, PaletteType.CHUNK, DataPalette.GLOBAL_PALETTE_BITS_PER_ENTRY);
        DataPalette biomePalette = new DataPalette(singletonPalette, null, PaletteType.BIOME, 4);
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
                decoded = helper.readChunkSection(buf, 4);
            } catch (Exception e) {
                System.out.println(section);
                e.printStackTrace();
                throw e;
            }
            Assert.assertEquals("Decoded packet does not match original: " + section + " vs " + decoded, section, decoded);
        }
    }
}

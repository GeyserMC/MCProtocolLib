package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.data.game.chunk.ChunkSection;
import com.github.steveice10.mc.protocol.data.game.chunk.DataPalette;
import com.github.steveice10.mc.protocol.data.game.chunk.palette.PaletteType;
import com.github.steveice10.mc.protocol.data.game.chunk.palette.SingletonPalette;
import com.github.steveice10.packetlib.io.stream.StreamNetInput;
import com.github.steveice10.packetlib.io.stream.StreamNetOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        for (ChunkSection section : chunkSectionsToTest) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            StreamNetOutput out = new StreamNetOutput(stream);
            ChunkSection.write(out, section, 4);
            StreamNetInput in = new StreamNetInput(new ByteArrayInputStream(stream.toByteArray()));
            ChunkSection decoded;
            try {
                decoded = ChunkSection.read(in, 4);
            } catch (Exception e) {
                System.out.println(section);
                e.printStackTrace();
                throw e;
            }
            Assert.assertEquals("Decoded packet does not match original: " + section + " vs " + decoded, section, decoded);
        }
    }
}

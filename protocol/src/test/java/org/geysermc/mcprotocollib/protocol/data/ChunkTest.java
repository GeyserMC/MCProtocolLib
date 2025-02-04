package org.geysermc.mcprotocollib.protocol.data;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.ChunkSection;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.DataPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.PaletteType;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.SingletonPalette;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ChunkTest {
    private static final Logger log = LoggerFactory.getLogger(ChunkTest.class);
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
    public void testChunkSectionEncoding() {
        for (ChunkSection section : chunkSectionsToTest) {
            ByteBuf buf = Unpooled.buffer();
            MinecraftTypes.writeChunkSection(buf, section);
            ChunkSection decoded;
            try {
                decoded = MinecraftTypes.readChunkSection(buf);
            } catch (Exception e) {
                log.error(section.toString(), e);
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

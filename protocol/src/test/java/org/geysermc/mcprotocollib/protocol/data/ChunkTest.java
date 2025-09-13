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
    // Arbitrary registry size values
    private static final int BLOCK_STATE_REGISTRY_SIZE = 1000;
    private static final int BIOME_REGISTRY_SIZE = 100;

    private static final Logger log = LoggerFactory.getLogger(ChunkTest.class);
    private final List<ChunkSection> chunkSectionsToTest = new ArrayList<>();

    @BeforeEach
    public void setup() {
        chunkSectionsToTest.add(new ChunkSection(420, BLOCK_STATE_REGISTRY_SIZE, 42, BIOME_REGISTRY_SIZE));

        ChunkSection section = new ChunkSection(20, BLOCK_STATE_REGISTRY_SIZE, 35, BIOME_REGISTRY_SIZE);
        section.setBlock(0, 0, 0, 10);
        chunkSectionsToTest.add(section);

        SingletonPalette singletonPalette = new SingletonPalette(20);
        DataPalette dataPalette = DataPalette.create(singletonPalette, null, PaletteType.CHUNK, BLOCK_STATE_REGISTRY_SIZE);
        DataPalette biomePalette = DataPalette.create(singletonPalette, null, PaletteType.BIOME, BIOME_REGISTRY_SIZE);
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
                decoded = MinecraftTypes.readChunkSection(buf, BLOCK_STATE_REGISTRY_SIZE, BIOME_REGISTRY_SIZE);
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

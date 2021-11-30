package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.data.game.chunk.palette.PaletteType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.*;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@EqualsAndHashCode
public class ChunkSection {

    private static final int AIR = 0;

    private int blockCount;
    private @NonNull DataPalette chunkData;
    @Getter
    private @NonNull DataPalette biomeData;

    public ChunkSection() {
        this(0, DataPalette.createForChunk(), DataPalette.createForBiome(4));
    }

    public static ChunkSection read(NetInput in, int globalBiomePaletteBits) throws IOException {
        int blockCount = in.readShort();

        DataPalette chunkPalette = DataPalette.read(in, PaletteType.CHUNK, DataPalette.GLOBAL_PALETTE_BITS_PER_ENTRY);
        DataPalette biomePalette = DataPalette.read(in, PaletteType.BIOME, globalBiomePaletteBits);
        return new ChunkSection(blockCount, chunkPalette, biomePalette);
    }

    public static void write(NetOutput out, ChunkSection section, int globalBiomePaletteBits) throws IOException {
        out.writeShort(section.blockCount);
        DataPalette.write(out, section.chunkData);
        DataPalette.write(out, section.biomeData);
    }

    public int getBlock(int x, int y, int z) {
        return this.chunkData.get(x, y, z);
    }

    public void setBlock(int x, int y, int z, int state) {
        int curr = this.chunkData.set(x, y, z, state);
        if (state != AIR && curr == AIR) {
            this.blockCount++;
        } else if (state == AIR && curr != AIR) {
            this.blockCount--;
        }
    }

    public boolean isBlockCountEmpty() {
        return this.blockCount == 0;
    }
}
package com.github.steveice10.mc.protocol.data.game.chunk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ChunkSection {
    private static final int AIR = 0;
    private final @NotNull DataPalette chunkData;
    private final @NotNull DataPalette biomeData;
    private int blockCount;

    public ChunkSection() {
        this(DataPalette.createForChunk(), DataPalette.createForBiome(4), 0);
    }

    public ChunkSection(ChunkSection original) {
        this( new DataPalette(original.chunkData), new DataPalette(original.biomeData), original.blockCount);
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

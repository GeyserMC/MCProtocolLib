package org.geysermc.mcprotocollib.protocol.data.game.chunk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@EqualsAndHashCode
public class ChunkSection {

    private static final int AIR = 0;

    private int blockCount;
    private int fluidCount;
    private @NonNull DataPalette blockData;
    @Getter
    private @NonNull DataPalette biomeData;

    public ChunkSection(int initialBlockState, int blockStateRegistrySize, int initialBiome, int biomeRegistrySize) {
        this(0, 0, DataPalette.createForBlockState(initialBlockState, blockStateRegistrySize), DataPalette.createForBiome(initialBiome, biomeRegistrySize));
    }

    public ChunkSection(ChunkSection original) {
        this(original.blockCount, original.fluidCount, new DataPalette(original.blockData), new DataPalette(original.biomeData));
    }

    public int getBlock(int x, int y, int z) {
        return this.blockData.get(x, y, z);
    }

    public void setBlock(int x, int y, int z, int state) {
        int curr = this.blockData.set(x, y, z, state);
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

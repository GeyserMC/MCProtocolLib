package org.geysermc.mcprotocollib.protocol.data.game.chunk;

import lombok.*;

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
        this(0, DataPalette.createForChunk(), DataPalette.createForBiome());
    }

    public ChunkSection(ChunkSection original) {
        this(original.blockCount, new DataPalette(original.chunkData), new DataPalette(original.biomeData));
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

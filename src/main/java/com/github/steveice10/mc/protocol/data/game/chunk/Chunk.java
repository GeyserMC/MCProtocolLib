package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.*;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class Chunk {
    private static final int MAX_PALETTE_BITS_PER_ENTRY = 8;
    private static final int MAX_BIOME_BITS_PER_ENTRY = 2;

    private static final int AIR = 0;

    private int blockCount;
    private @NonNull DataPalette chunkData;
    @Getter
    private @NonNull DataPalette biomeData;

    public Chunk() {
        this(0, DataPalette.createEmpty(MAX_PALETTE_BITS_PER_ENTRY, DataPalette.CHUNK_SIZE), DataPalette.createEmpty(MAX_BIOME_BITS_PER_ENTRY, DataPalette.BIOME_SIZE));
    }

    public static Chunk read(NetInput in) throws IOException {
        int blockCount = in.readShort();

        DataPalette chunkPalette = DataPalette.read(in, MAX_PALETTE_BITS_PER_ENTRY, DataPalette.CHUNK_SIZE);
        DataPalette biomePalette = DataPalette.read(in, MAX_BIOME_BITS_PER_ENTRY, DataPalette.BIOME_SIZE);
        return new Chunk(blockCount, chunkPalette, biomePalette);
    }

    public static void write(NetOutput out, Chunk chunk) throws IOException {
        out.writeShort(chunk.blockCount);
        DataPalette.write(out, chunk.chunkData);
        DataPalette.write(out, chunk.biomeData);
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

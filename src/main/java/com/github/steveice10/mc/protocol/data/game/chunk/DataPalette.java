package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.data.game.chunk.palette.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DataPalette {
    public static final int GLOBAL_PALETTE_BITS_PER_ENTRY = 14;
    private final PaletteType paletteType;
    private final int globalPaletteBits;
    private @NonNull Palette palette;
    private BitStorage storage;

    public DataPalette(DataPalette original) {
        this(original.paletteType, original.globalPaletteBits, original.palette.copy(), original.storage == null ? null : new BitStorage(original.storage));
    }

    public static DataPalette createForChunk() {
        return createForChunk(GLOBAL_PALETTE_BITS_PER_ENTRY);
    }

    public static DataPalette createForChunk(int globalPaletteBits) {
        return createEmpty(PaletteType.CHUNK, globalPaletteBits);
    }

    public static DataPalette createForBiome(int globalPaletteBits) {
        return createEmpty(PaletteType.BIOME, globalPaletteBits);
    }

    public static DataPalette createEmpty(PaletteType paletteType, int globalPaletteBits) {
        return new DataPalette(paletteType, globalPaletteBits, new ListPalette(paletteType.getMinBitsPerEntry()),
                new BitStorage(paletteType.getMinBitsPerEntry(), paletteType.getStorageSize()));
    }

    private static Palette createPalette(int bitsPerEntry, PaletteType paletteType) {
        if (bitsPerEntry <= paletteType.getMinBitsPerEntry()) {
            return new ListPalette(bitsPerEntry);
        } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntry()) {
            return new MapPalette(bitsPerEntry);
        } else {
            return new GlobalPalette();
        }
    }

    private static int index(int x, int y, int z) {
        return y << 8 | z << 4 | x;
    }

    public int get(int x, int y, int z) {
        if (storage != null) {
            int id = this.storage.get(index(x, y, z));
            return this.palette.idToState(id);
        } else {
            return this.palette.idToState(0);
        }
    }

    /**
     * Sets the block state at the given coordinates.
     *
     * @param x     the x coordinate.
     * @param y     the y coordinate.
     * @param z     the z coordinate.
     * @param state the new block state.
     * @return the old value present in the storage.
     */
    public int set(int x, int y, int z, int state) {
        int id = this.palette.stateToId(state);
        if (id == -1) {
            resize();
            id = this.palette.stateToId(state);
        }

        if (this.storage != null) {
            int index = index(x, y, z);
            int curr = this.storage.get(index);

            this.storage.set(index, id);
            return curr;
        } else {
            // Singleton palette and the block has not changed because the palette hasn't resized
            return state;
        }
    }

    private int sanitizeBitsPerEntry(int bitsPerEntry) {
        if (bitsPerEntry <= this.paletteType.getMaxBitsPerEntry()) {
            return Math.max(this.paletteType.getMinBitsPerEntry(), bitsPerEntry);
        } else {
            return globalPaletteBits;
        }
    }

    private void resize() {
        Palette oldPalette = this.palette;
        BitStorage oldData = this.storage;

        int bitsPerEntry = sanitizeBitsPerEntry(oldPalette instanceof SingletonPalette ? 1 : oldData.getBitsPerEntry() + 1);
        this.palette = createPalette(bitsPerEntry, paletteType);
        this.storage = new BitStorage(bitsPerEntry, paletteType.getStorageSize());

        if (oldPalette instanceof SingletonPalette) {
            this.palette.stateToId(oldPalette.idToState(0));
        } else {
            for (int i = 0; i < paletteType.getStorageSize(); i++) {
                this.storage.set(i, this.palette.stateToId(oldPalette.idToState(oldData.get(i))));
            }
        }
    }
}

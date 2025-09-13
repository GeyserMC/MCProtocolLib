package org.geysermc.mcprotocollib.protocol.data.game.chunk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.GlobalPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.ListPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.MapPalette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.Palette;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.PaletteType;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.palette.SingletonPalette;

@Getter
@EqualsAndHashCode
@ToString
public class DataPalette {
    private static final double LOG_2 = Math.log(2.0);

    private @NonNull Palette palette;
    private BitStorage storage;
    private final PaletteType paletteType;
    private final int globalPaletteBitsPerEntry;

    private DataPalette(@NonNull Palette palette, BitStorage storage, PaletteType paletteType, int globalPaletteBitsPerEntry) {
        this.palette = palette;
        this.storage = storage;
        this.paletteType = paletteType;
        this.globalPaletteBitsPerEntry = globalPaletteBitsPerEntry;
    }

    public DataPalette(DataPalette original) {
        this(original.palette.copy(), original.storage == null ? null : new BitStorage(original.storage), original.paletteType, original.globalPaletteBitsPerEntry);
    }

    public static DataPalette createForChunk(int initialState, int blockStateRegistrySize) {
        return createEmpty(PaletteType.CHUNK, initialState, blockStateRegistrySize);
    }

    public static DataPalette createForBiome(int initialBiome, int biomeRegistrySize) {
        return createEmpty(PaletteType.BIOME, initialBiome, biomeRegistrySize);
    }

    public static DataPalette createEmpty(PaletteType paletteType, int initial, int registrySize) {
        return create(new SingletonPalette(initial), null, paletteType, registrySize);
    }

    public static DataPalette create(@NonNull Palette palette, BitStorage storage, PaletteType paletteType, int registrySize) {
        return new DataPalette(palette, storage, paletteType, calculateBitsPerEntry(registrySize));
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
            return globalPaletteBitsPerEntry;
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

    private static Palette createPalette(int bitsPerEntry, PaletteType paletteType) {
        if (bitsPerEntry <= paletteType.getMinBitsPerEntry()) {
            return new ListPalette(bitsPerEntry);
        } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntry()) {
            return new MapPalette(bitsPerEntry);
        } else {
            return new GlobalPalette();
        }
    }

    private int index(int x, int y, int z) {
        return y << paletteType.getMaxBitsPerEntry() | z << paletteType.getMinBitsPerEntry() | x;
    }

    private static int calculateBitsPerEntry(int registrySize) {
        // Mojmap uses Mth.ceillog2
        return (int) Math.ceil(Math.log(registrySize) / LOG_2);
    }
}

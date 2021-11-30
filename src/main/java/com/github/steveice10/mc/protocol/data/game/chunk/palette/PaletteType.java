package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import lombok.Getter;

@Getter
public enum PaletteType {
    BIOME(1, 3, 64),
    CHUNK(4, 8, 4096);

    private final int minBitsPerEntry;
    private final int maxBitsPerEntry;
    private final int storageSize;

    PaletteType(int minBitsPerEntry, int maxBitsPerEntry, int storageSize) {
        this.minBitsPerEntry = minBitsPerEntry;
        this.maxBitsPerEntry = maxBitsPerEntry;
        this.storageSize = storageSize;
    }
}

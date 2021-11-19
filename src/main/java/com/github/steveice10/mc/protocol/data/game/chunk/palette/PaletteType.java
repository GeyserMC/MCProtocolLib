package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import lombok.Getter;

@Getter
public enum PaletteType {
    BIOME(3, 64),
    CHUNK(8, 4096);

    private final int maxBitsPerEntry;
    private final int storageSize;

    PaletteType(int maxBitsPerEntry, int storageSize) {
        this.maxBitsPerEntry = maxBitsPerEntry;
        this.storageSize = storageSize;
    }
}

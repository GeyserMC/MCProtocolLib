package com.github.steveice10.mc.protocol.data.game.chunk.palette;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaletteType {
    BIOME(1, 3, 64),
    CHUNK(4, 8, 4096);

    private final int minBitsPerEntry;
    private final int maxBitsPerEntry;
    private final int storageSize;
}

package com.github.steveice10.mc.protocol.data.game.chunk;

import lombok.Data;
import lombok.NonNull;
import net.kyori.adventure.nbt.CompoundBinaryTag;

import java.util.Arrays;

@Data
public class Column {
    private final int x;
    private final int z;
    private final @NonNull Chunk[] chunks;
    private final @NonNull CompoundBinaryTag[] tileEntities;
    private final @NonNull CompoundBinaryTag heightMaps;
    private final int[] biomeData;

    public Column(int x, int z, @NonNull Chunk[] chunks, @NonNull CompoundBinaryTag[] tileEntities, @NonNull CompoundBinaryTag heightMaps) {
        this(x, z, chunks, tileEntities, heightMaps, null);
    }

    public Column(int x, int z, @NonNull Chunk[] chunks, @NonNull CompoundBinaryTag[] tileEntities, @NonNull CompoundBinaryTag heightMaps, int[] biomeData) {
        if(chunks.length != 16) {
            throw new IllegalArgumentException("Chunk array length must be 16.");
        }

        this.x = x;
        this.z = z;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.biomeData = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
        this.tileEntities = tileEntities != null ? tileEntities : new CompoundBinaryTag[0];
        this.heightMaps = heightMaps;
    }
}

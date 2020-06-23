package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.Arrays;

@Data
public class Column {
    private final int x;
    private final int z;
    private final boolean ignoreOldData;
    @EqualsAndHashCode.Exclude
    private final @NonNull Chunk[] chunks;
    private final @NonNull CompoundTag[] tileEntities;
    private final @NonNull CompoundTag heightMaps;
    private final int[] biomeData;

    public Column(int x, int z, boolean ignoreOldData, @NonNull Chunk[] chunks, @NonNull CompoundTag[] tileEntities, @NonNull CompoundTag heightMaps) {
        this(x, z, ignoreOldData, chunks, tileEntities, heightMaps, null);
    }

    public Column(int x, int z, boolean ignoreOldData, @NonNull Chunk[] chunks, @NonNull CompoundTag[] tileEntities, @NonNull CompoundTag heightMaps, int[] biomeData) {
        if(chunks.length != 16) {
            throw new IllegalArgumentException("Chunk array length must be 16.");
        }

        if(biomeData != null && biomeData.length != 1024) {
            throw new IllegalArgumentException("Biome data array length must be 1024.");
        }

        this.x = x;
        this.z = z;
        this.ignoreOldData = ignoreOldData;
        this.chunks = Arrays.copyOf(chunks, chunks.length);
        this.biomeData = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
        this.tileEntities = tileEntities != null ? tileEntities : new CompoundTag[0];
        this.heightMaps = heightMaps;
    }
}

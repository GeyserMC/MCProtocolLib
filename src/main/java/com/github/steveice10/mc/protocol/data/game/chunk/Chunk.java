package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;

@Data
public class Chunk {
    private final int x;
    private final int z;
    private final @NonNull ChunkSection[] sections;
    private final @NonNull CompoundTag[] blockEntities;
    private final @NonNull CompoundTag heightMaps;
    private final @NonNull int[] biomeData;

    /**
     * @deprecated Non-full chunks no longer exist since 1.17.
     */
    @Deprecated
    public Chunk(int x, int z, @NonNull ChunkSection[] sections, @NonNull CompoundTag[] blockEntities, @NonNull CompoundTag heightMaps) {
        this(x, z, sections, blockEntities, heightMaps, new int[1024]);
    }

    public Chunk(int x, int z, @NonNull ChunkSection[] sections, @NonNull CompoundTag[] blockEntities, @NonNull CompoundTag heightMaps, @NonNull int[] biomeData) {
        this.x = x;
        this.z = z;
        this.sections = Arrays.copyOf(sections, sections.length);
        this.biomeData = biomeData != null ? Arrays.copyOf(biomeData, biomeData.length) : null;
        this.blockEntities = blockEntities != null ? blockEntities : new CompoundTag[0];
        this.heightMaps = heightMaps;
    }
}

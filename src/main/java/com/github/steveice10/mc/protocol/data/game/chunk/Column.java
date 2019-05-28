package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;

import java.util.Arrays;

public class Column {
    private int x;
    private int z;
    private Chunk chunks[];
    private int biomeData[];
    private CompoundTag tileEntities[];
    private CompoundTag heightmaps;

    private boolean skylight;

    public Column(int x, int z, Chunk chunks[], CompoundTag[] tileEntities, CompoundTag heightmaps) {
        this(x, z, chunks, null, tileEntities, heightmaps);
    }

    public Column(int x, int z, Chunk chunks[], int biomeData[], CompoundTag[] tileEntities, CompoundTag heightmaps) {
        if(chunks.length != 16) {
            throw new IllegalArgumentException("Chunk array length must be 16.");
        }

        if(biomeData != null && biomeData.length != 256) {
            throw new IllegalArgumentException("Biome data array length must be 256.");
        }

        this.x = x;
        this.z = z;
        this.chunks = chunks;
        this.biomeData = biomeData;
        this.tileEntities = tileEntities != null ? tileEntities : new CompoundTag[0];
        this.heightmaps = heightmaps;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public boolean hasBiomeData() {
        return this.biomeData != null;
    }

    public int[] getBiomeData() {
        return this.biomeData;
    }

    public CompoundTag[] getTileEntities() {
        return this.tileEntities;
    }

    public boolean hasSkylight() {
        return this.skylight;
    }

    public CompoundTag getHeightmaps() {
        return heightmaps;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Column)) return false;

        Column that = (Column) o;
        return this.x == that.x &&
                this.z == that.z &&
                Arrays.equals(this.chunks, that.chunks) &&
                Arrays.equals(this.biomeData, that.biomeData) &&
                Arrays.equals(this.tileEntities, that.tileEntities);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.x, this.z, this.chunks, this.biomeData, this.tileEntities);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

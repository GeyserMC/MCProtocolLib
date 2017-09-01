package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;

import java.util.Arrays;

public class Column {
    private int x;
    private int z;
    private Chunk chunks[];
    private byte biomeData[];
    private CompoundTag tileEntities[];

    private boolean skylight;

    public Column(int x, int z, Chunk chunks[], CompoundTag[] tileEntities) {
        this(x, z, chunks, null, tileEntities);
    }

    public Column(int x, int z, Chunk chunks[], byte biomeData[], CompoundTag[] tileEntities) {
        if(chunks.length != 16) {
            throw new IllegalArgumentException("Chunk array length must be 16.");
        }

        if(biomeData != null && biomeData.length != 256) {
            throw new IllegalArgumentException("Biome data array length must be 256.");
        }

        this.skylight = false;
        boolean noSkylight = false;
        for(Chunk chunk : chunks) {
            if(chunk != null) {
                if(chunk.getSkyLight() == null) {
                    noSkylight = true;
                } else {
                    this.skylight = true;
                }
            }
        }

        if(noSkylight && this.skylight) {
            throw new IllegalArgumentException("Either all chunks must have skylight values or none must have them.");
        }

        this.x = x;
        this.z = z;
        this.chunks = chunks;
        this.biomeData = biomeData;
        this.tileEntities = tileEntities != null ? tileEntities : new CompoundTag[0];
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

    public byte[] getBiomeData() {
        return this.biomeData;
    }

    public CompoundTag[] getTileEntities() {
        return this.tileEntities;
    }

    public boolean hasSkylight() {
        return this.skylight;
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

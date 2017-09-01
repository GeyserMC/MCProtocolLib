package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

import java.util.Objects;

public class Chunk {
    private BlockStorage blocks;
    private NibbleArray3d blocklight;
    private NibbleArray3d skylight;

    public Chunk(boolean skylight) {
        this(new BlockStorage(), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null);
    }

    public Chunk(BlockStorage blocks, NibbleArray3d blocklight, NibbleArray3d skylight) {
        this.blocks = blocks;
        this.blocklight = blocklight;
        this.skylight = skylight;
    }

    public BlockStorage getBlocks() {
        return this.blocks;
    }

    public NibbleArray3d getBlockLight() {
        return this.blocklight;
    }

    public NibbleArray3d getSkyLight() {
        return this.skylight;
    }

    public boolean isEmpty() {
        return this.blocks.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Chunk)) return false;

        Chunk that = (Chunk) o;
        return Objects.equals(this.blocks, that.blocks) &&
                Objects.equals(this.blocklight, that.blocklight) &&
                Objects.equals(this.skylight, that.skylight);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.blocks, this.blocklight, this.skylight);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

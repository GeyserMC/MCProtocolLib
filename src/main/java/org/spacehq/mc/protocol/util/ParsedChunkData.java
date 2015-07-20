package org.spacehq.mc.protocol.util;

import org.spacehq.mc.protocol.data.game.Chunk;

import java.util.Arrays;

public class ParsedChunkData {

    private Chunk chunks[];
    private byte biomes[];

    public ParsedChunkData(Chunk chunks[], byte biomes[]) {
        this.chunks = chunks;
        this.biomes = biomes;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public byte[] getBiomes() {
        return this.biomes;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        ParsedChunkData that = (ParsedChunkData) o;

        if(!Arrays.equals(biomes, that.biomes)) return false;
        if(!Arrays.equals(chunks, that.chunks)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(chunks);
        result = 31 * result + (biomes != null ? Arrays.hashCode(biomes) : 0);
        return result;
    }

}

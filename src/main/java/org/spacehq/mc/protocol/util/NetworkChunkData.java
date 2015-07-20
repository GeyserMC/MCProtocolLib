package org.spacehq.mc.protocol.util;

public class NetworkChunkData {

    private int mask;
    private boolean fullChunk;
    private boolean sky;
    private byte data[];

    public NetworkChunkData(int mask, boolean fullChunk, boolean sky, byte data[]) {
        this.mask = mask;
        this.fullChunk = fullChunk;
        this.sky = sky;
        this.data = data;
    }

    public int getMask() {
        return this.mask;
    }

    public boolean isFullChunk() {
        return this.fullChunk;
    }

    public boolean hasSkyLight() {
        return this.sky;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        NetworkChunkData that = (NetworkChunkData) o;

        if(fullChunk != that.fullChunk) return false;
        if(mask != that.mask) return false;
        if(sky != that.sky) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mask;
        result = 31 * result + (fullChunk ? 1 : 0);
        result = 31 * result + (sky ? 1 : 0);
        return result;
    }

}

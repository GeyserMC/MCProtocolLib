package org.spacehq.mc.protocol.data.game.values.entity;

public class FallingBlockData implements ObjectData {

    private int id;
    private int metadata;

    public FallingBlockData(int id, int metadata) {
        this.id = id;
        this.metadata = metadata;
    }

    public int getId() {
        return this.id;
    }

    public int getMetadata() {
        return this.metadata;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        FallingBlockData that = (FallingBlockData) o;

        if(id != that.id) return false;
        if(metadata != that.metadata) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + metadata;
        return result;
    }

}

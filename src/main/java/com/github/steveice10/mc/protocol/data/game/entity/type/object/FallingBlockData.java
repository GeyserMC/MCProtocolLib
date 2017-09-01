package com.github.steveice10.mc.protocol.data.game.entity.type.object;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

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
        if(!(o instanceof FallingBlockData)) return false;

        FallingBlockData that = (FallingBlockData) o;
        return this.id == that.id &&
                this.metadata == that.metadata;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.id, this.metadata);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

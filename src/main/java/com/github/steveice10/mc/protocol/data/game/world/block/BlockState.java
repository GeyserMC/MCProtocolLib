package com.github.steveice10.mc.protocol.data.game.world.block;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class BlockState {
    private int id;

    public BlockState(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof BlockState)) return false;

        BlockState that = (BlockState) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.id);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

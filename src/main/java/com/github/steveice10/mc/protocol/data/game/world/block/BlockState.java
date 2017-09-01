package com.github.steveice10.mc.protocol.data.game.world.block;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class BlockState {
    private int id;
    private int data;

    public BlockState(int id, int data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return this.id;
    }

    public int getData() {
        return this.data;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof BlockState)) return false;

        BlockState that = (BlockState) o;
        return this.id == that.id &&
                this.data == that.data;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.id, this.data);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

package com.github.steveice10.mc.protocol.data.game.world.block;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

import java.util.Objects;

public class BlockChangeRecord {
    private Position position;
    private BlockState block;

    public BlockChangeRecord(Position position, BlockState block) {
        this.position = position;
        this.block = block;
    }

    public Position getPosition() {
        return this.position;
    }

    public BlockState getBlock() {
        return this.block;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof BlockChangeRecord)) return false;

        BlockChangeRecord that = (BlockChangeRecord) o;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.block, that.block);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.position, this.block);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

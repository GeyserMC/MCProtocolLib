package org.spacehq.mc.protocol.data.game.world.block;

import org.spacehq.mc.protocol.data.game.entity.metadata.Position;

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
        return o instanceof BlockChangeRecord && this.position.equals(((BlockChangeRecord) o).position) && this.block.equals(((BlockChangeRecord) o).block);
    }

    @Override
    public int hashCode() {
        int result = this.position.hashCode();
        result = 31 * result + this.block.hashCode();
        return result;
    }
}

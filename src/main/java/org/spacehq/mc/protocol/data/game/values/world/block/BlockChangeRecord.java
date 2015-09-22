package org.spacehq.mc.protocol.data.game.values.world.block;

import org.spacehq.mc.protocol.data.game.Position;

public class BlockChangeRecord {
    private Position position;
    private int id;
    private int data;

    public BlockChangeRecord(Position position, int id, int data) {
        this.position = position;
        this.id = id;
        this.data = data;
    }

    public Position getPosition() {
        return this.position;
    }

    public int getId() {
        return this.id;
    }

    public int getData() {
        return this.data;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof BlockChangeRecord && this.position.equals(((BlockChangeRecord) o).position) && this.id == ((BlockChangeRecord) o).id && this.data == ((BlockChangeRecord) o).data);
    }

    @Override
    public int hashCode() {
        int result = this.position.hashCode();
        result = 31 * result + this.id;
        result = 31 * result + this.data;
        return result;
    }
}

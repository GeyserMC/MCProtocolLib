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
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        BlockChangeRecord record = (BlockChangeRecord) o;

        if(id != record.id) return false;
        if(data != record.data) return false;
        if(!position.equals(record.position)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + id;
        result = 31 * result + data;
        return result;
    }

}

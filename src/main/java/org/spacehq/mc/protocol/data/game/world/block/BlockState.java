package org.spacehq.mc.protocol.data.game.world.block;

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
        return o instanceof BlockState && this.id == ((BlockState) o).id && this.data == ((BlockState) o).data;
    }

    @Override
    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.data;
        return result;
    }
}

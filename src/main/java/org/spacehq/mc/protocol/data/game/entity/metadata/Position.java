package org.spacehq.mc.protocol.data.game.entity.metadata;

import org.spacehq.mc.protocol.util.ReflectionToString;

public class Position {
    private int x;
    private int y;
    private int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Position && this.x == ((Position) o).x && this.y == ((Position) o).y && this.z == ((Position) o).z);
    }

    @Override
    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}

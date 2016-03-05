package org.spacehq.mc.protocol.data.game.world.map;

import java.util.Arrays;

public class MapData {
    private int columns;
    private int rows;
    private int x;
    private int y;
    private byte data[];

    public MapData(int columns, int rows, int x, int y, byte data[]) {
        this.columns = columns;
        this.rows = rows;
        this.x = x;
        this.y = y;
        this.data = data;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getRows() {
        return this.rows;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MapData && this.columns == ((MapData) o).columns && this.rows == ((MapData) o).rows && this.x == ((MapData) o).x && this.y == ((MapData) o).y && Arrays.equals(this.data, ((MapData) o).data);
    }

    @Override
    public int hashCode() {
        int result = this.columns;
        result = 31 * result + this.rows;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + Arrays.hashCode(this.data);
        return result;
    }
}

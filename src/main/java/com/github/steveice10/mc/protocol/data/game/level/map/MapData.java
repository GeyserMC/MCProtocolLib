package com.github.steveice10.mc.protocol.data.game.level.map;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public record MapData(int columns, int rows, int x, int y, byte @NotNull [] data) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapData that)) return false;
        return columns == that.columns && rows == that.rows && x == that.x && y == that.y && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(columns, rows, x, y);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}

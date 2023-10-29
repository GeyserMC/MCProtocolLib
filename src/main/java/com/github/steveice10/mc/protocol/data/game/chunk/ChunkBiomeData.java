package com.github.steveice10.mc.protocol.data.game.chunk;

import java.util.Arrays;
import java.util.Objects;

public record ChunkBiomeData(int x, int z, byte[] buffer) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkBiomeData that)) return false;
        return x == that.x && z == that.z && Arrays.equals(buffer, that.buffer);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(x, z);
        result = 31 * result + Arrays.hashCode(buffer);
        return result;
    }
}

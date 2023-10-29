package com.github.steveice10.mc.protocol.data.game;

import java.util.Arrays;
import java.util.Objects;

public record ArgumentSignature(String name, byte[] signature) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArgumentSignature that = (ArgumentSignature) o;
        return Objects.equals(name, that.name) && Arrays.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(signature);
        return result;
    }
}

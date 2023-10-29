package com.github.steveice10.packetlib;

public record Flag<T>(String name, Class<T> clazz) {
    public T cast(Object value) {
        if (value == null) {
            return null;
        }

        if (!this.clazz.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Value is not of type " + this.clazz.getName() + ". Value: " + value);
        }

        return this.clazz.cast(value);
    }
}

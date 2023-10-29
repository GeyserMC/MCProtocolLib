package com.github.steveice10.mc.protocol.data.game.command.properties;

public record FloatProperties(float min, float max) implements CommandProperties {
    public FloatProperties() {
        this(-Float.MAX_VALUE, Float.MAX_VALUE);
    }
}

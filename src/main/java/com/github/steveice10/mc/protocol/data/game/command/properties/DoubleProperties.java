package com.github.steveice10.mc.protocol.data.game.command.properties;

public record DoubleProperties(double min, double max) implements CommandProperties {
    public DoubleProperties() {
        this(-Double.MAX_VALUE, Double.MAX_VALUE);
    }
}

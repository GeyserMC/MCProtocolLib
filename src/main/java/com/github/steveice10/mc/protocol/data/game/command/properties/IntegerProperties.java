package com.github.steveice10.mc.protocol.data.game.command.properties;

public record IntegerProperties(int min, int max) implements CommandProperties {
    public IntegerProperties() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}

package com.github.steveice10.mc.protocol.data.game.command.properties;

public enum StringProperties implements CommandProperties {
    SINGLE_WORD,
    QUOTABLE_PHRASE,
    GREEDY_PHRASE;

    public static final StringProperties[] VALUES = values();

    public static StringProperties from(int id) {
        return VALUES[id];
    }
}

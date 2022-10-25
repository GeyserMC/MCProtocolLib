package com.github.steveice10.mc.protocol.data.game.chat;

public enum ChatFilterType {
    PASS_THROUGH,
    FULLY_FILTERED,
    PARTIALLY_FILTERED;

    public static final ChatFilterType[] VALUES = values();

    public static ChatFilterType from(int id) {
        return VALUES[id];
    }
}

package com.github.steveice10.mc.protocol.data.game.level.block;

public enum CommandBlockMode {
    SEQUENCE,
    AUTO,
    REDSTONE;

    private static final CommandBlockMode[] VALUES = values();

    public static CommandBlockMode from(int id) {
        return VALUES[id];
    }
}

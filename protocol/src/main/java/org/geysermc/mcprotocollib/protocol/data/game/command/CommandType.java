package org.geysermc.mcprotocollib.protocol.data.game.command;

public enum CommandType {
    ROOT,
    LITERAL,
    ARGUMENT;

    private static final CommandType[] VALUES = values();

    public static CommandType from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }
}

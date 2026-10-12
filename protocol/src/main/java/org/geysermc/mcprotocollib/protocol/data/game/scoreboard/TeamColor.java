package org.geysermc.mcprotocollib.protocol.data.game.scoreboard;

public enum TeamColor {
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_AQUA,
    DARK_RED,
    DARK_PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    GREEN,
    AQUA,
    RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE;

    private static final TeamColor[] VALUES = values();

    public static TeamColor from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }
}

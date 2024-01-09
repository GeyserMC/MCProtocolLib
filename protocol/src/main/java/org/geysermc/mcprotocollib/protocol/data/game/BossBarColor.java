package org.geysermc.mcprotocollib.protocol.data.game;

public enum BossBarColor {
    PINK,
    CYAN,
    RED,
    LIME,
    YELLOW,
    PURPLE,
    WHITE;

    private static final BossBarColor[] VALUES = values();

    public static BossBarColor from(int id) {
        return VALUES[id];
    }
}

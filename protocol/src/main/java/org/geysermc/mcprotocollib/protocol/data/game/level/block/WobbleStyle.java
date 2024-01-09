package org.geysermc.mcprotocollib.protocol.data.game.level.block;

public enum WobbleStyle {
    POSITIVE,
    NEGATIVE;

    private static final WobbleStyle[] VALUES = values();

    public static WobbleStyle from(int id) {
        return VALUES[id];
    }
}

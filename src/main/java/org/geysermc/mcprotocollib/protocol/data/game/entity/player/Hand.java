package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

public enum Hand {
    MAIN_HAND,
    OFF_HAND;

    private static final Hand[] VALUES = values();

    public static Hand from(int id) {
        return VALUES[id];
    }
}

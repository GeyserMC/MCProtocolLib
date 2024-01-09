package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

public enum HandPreference {
    LEFT_HAND,
    RIGHT_HAND;

    private static final HandPreference[] VALUES = values();

    public static HandPreference from(int id) {
        return VALUES[id];
    }
}

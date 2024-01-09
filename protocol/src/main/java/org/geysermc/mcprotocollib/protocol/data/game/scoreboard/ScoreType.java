package org.geysermc.mcprotocollib.protocol.data.game.scoreboard;

public enum ScoreType {
    INTEGER,
    HEARTS;

    private static final ScoreType[] VALUES = values();

    public static ScoreType from(int id) {
        return VALUES[id];
    }
}

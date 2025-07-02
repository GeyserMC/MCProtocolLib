package org.geysermc.mcprotocollib.protocol.data.game.setting;

public enum Difficulty {
    PEACEFUL,
    EASY,
    NORMAL,
    HARD;

    private static final Difficulty[] VALUES = values();

    public static Difficulty from(int id) {
        return VALUES[Math.floorMod(id, VALUES.length)];
    }
}

package org.geysermc.mcprotocollib.protocol.data.game;

public enum BossBarDivision {
    NONE,
    NOTCHES_6,
    NOTCHES_10,
    NOTCHES_12,
    NOTCHES_20;

    private static final BossBarDivision[] VALUES = values();

    public static BossBarDivision from(int id) {
        return VALUES[id];
    }
}

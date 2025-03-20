package org.geysermc.mcprotocollib.protocol.data.game.scoreboard;

public enum NameTagVisibility {
    ALWAYS,
    NEVER,
    HIDE_FOR_OTHER_TEAMS,
    HIDE_FOR_OWN_TEAM;

    private static final NameTagVisibility[] VALUES = values();

    public static NameTagVisibility from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }
}

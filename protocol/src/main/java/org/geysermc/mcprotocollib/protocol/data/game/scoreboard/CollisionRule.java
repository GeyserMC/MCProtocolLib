package org.geysermc.mcprotocollib.protocol.data.game.scoreboard;

public enum CollisionRule {
    ALWAYS,
    NEVER,
    PUSH_OTHER_TEAMS,
    PUSH_OWN_TEAM;

    private static final CollisionRule[] VALUES = values();

    public static CollisionRule from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }
}

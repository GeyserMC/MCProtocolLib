package org.geysermc.mcprotocollib.protocol.data.game.scoreboard;

import java.util.HashMap;
import java.util.Map;

public enum CollisionRule {
    ALWAYS("always"),
    NEVER("never"),
    PUSH_OTHER_TEAMS("pushOtherTeams"),
    PUSH_OWN_TEAM("pushOwnTeam");

    private static final Map<String, CollisionRule> VALUES = new HashMap<>();

    static {
        for (CollisionRule rule : values()) {
            VALUES.put(rule.getName(), rule);
        }
    }

    private final String name;

    CollisionRule(String name) {
        this.name = name;
    }

    public static CollisionRule from(String name) {
        return VALUES.get(name);
    }

    public String getName() {
        return this.name;
    }
}

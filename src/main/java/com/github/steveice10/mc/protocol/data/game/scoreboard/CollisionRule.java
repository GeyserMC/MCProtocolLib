package com.github.steveice10.mc.protocol.data.game.scoreboard;

import java.util.HashMap;
import java.util.Map;

public enum CollisionRule {
    ALWAYS("always"),
    NEVER("never"),
    PUSH_OTHER_TEAMS("pushOtherTeams"),
    PUSH_OWN_TEAM("pushOwnTeam");

    private final String name;

    CollisionRule(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, CollisionRule> VALUES = new HashMap<>();

    public static CollisionRule from(String name) {
        return VALUES.get(name);
    }

    static {
        for (CollisionRule rule : values()) {
            VALUES.put(rule.getName(), rule);
        }
    }
}

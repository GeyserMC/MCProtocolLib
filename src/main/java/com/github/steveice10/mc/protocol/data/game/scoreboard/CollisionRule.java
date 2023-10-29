package com.github.steveice10.mc.protocol.data.game.scoreboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
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

    public static CollisionRule from(String name) {
        return VALUES.get(name);
    }
}

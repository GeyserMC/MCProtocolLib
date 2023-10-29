package com.github.steveice10.mc.protocol.data.game.scoreboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum NameTagVisibility {
    ALWAYS("always"),
    NEVER("never"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam");

    private static final Map<String, NameTagVisibility> VALUES = new HashMap<>();

    static {
        for (NameTagVisibility option : values()) {
            VALUES.put(option.getName(), option);
        }
    }

    private final String name;

    public static NameTagVisibility from(String name) {
        return VALUES.get(name);
    }
}

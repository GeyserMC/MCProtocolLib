package com.github.steveice10.mc.protocol.data.game.scoreboard;

import java.util.HashMap;
import java.util.Map;

public enum NameTagVisibility {
    ALWAYS("always"),
    NEVER("never"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam");

    private final String name;

    NameTagVisibility(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, NameTagVisibility> VALUES = new HashMap<>();

    public static NameTagVisibility from(String name) {
        return VALUES.get(name);
    }

    static {
        for (NameTagVisibility option : values()) {
            VALUES.put(option.getName(), option);
        }
    }
}

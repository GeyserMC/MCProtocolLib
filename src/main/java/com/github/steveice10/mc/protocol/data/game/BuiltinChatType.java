package com.github.steveice10.mc.protocol.data.game;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum BuiltinChatType {
    CHAT,
    SYSTEM,
    GAME_INFO,
    SAY_COMMAND,
    MSG_COMMAND,
    TEAM_MSG_COMMAND,
    EMOTE_COMMAND,
    TELLRAW_COMMAND;

    private final String resourceLocation;

    BuiltinChatType() {
        this.resourceLocation = "minecraft:" + name().toLowerCase(Locale.ROOT);
    }

    public String getResourceLocation() {
        return resourceLocation;
    }
    private static final Map<String, BuiltinChatType> BY_RESOURCE_LOCATION;

    static {
        BuiltinChatType[] values = values();
        BY_RESOURCE_LOCATION = new HashMap<>(values.length);
        for (BuiltinChatType type : values) {
            BY_RESOURCE_LOCATION.put(type.getResourceLocation(), type);
        }
    }

    public static BuiltinChatType from(String resourceLocation) {
        return BY_RESOURCE_LOCATION.get(resourceLocation);
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.chat;

import net.kyori.adventure.key.Key;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum BuiltinChatType {
    CHAT,
    SAY_COMMAND,
    MSG_COMMAND_INCOMING,
    MSG_COMMAND_OUTGOING,
    TEAM_MSG_COMMAND_INCOMING,
    TEAM_MSG_COMMAND_OUTGOING,
    EMOTE_COMMAND;

    private final Key resourceLocation;

    BuiltinChatType() {
        this.resourceLocation = Key.key(name().toLowerCase(Locale.ROOT));
    }

    public Key getResourceLocation() {
        return resourceLocation;
    }

    private static final Map<Key, BuiltinChatType> VALUES = new HashMap<>();

    public static BuiltinChatType from(Key resourceLocation) {
        return VALUES.get(resourceLocation);
    }

    static {
        for (BuiltinChatType type : values()) {
            VALUES.put(type.getResourceLocation(), type);
        }
    }
}

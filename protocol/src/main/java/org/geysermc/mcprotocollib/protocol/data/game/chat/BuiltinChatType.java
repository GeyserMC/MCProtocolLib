package org.geysermc.mcprotocollib.protocol.data.game.chat;

import org.geysermc.mcprotocollib.protocol.data.game.Identifier;

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

    private final String resourceLocation;

    BuiltinChatType() {
        this.resourceLocation = Identifier.formalize(name().toLowerCase(Locale.ROOT));
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    private static final Map<String, BuiltinChatType> VALUES = new HashMap<>();

    public static BuiltinChatType from(String resourceLocation) {
        return VALUES.get(resourceLocation);
    }

    static {
        for (BuiltinChatType type : values()) {
            VALUES.put(type.getResourceLocation(), type);
        }
    }
}

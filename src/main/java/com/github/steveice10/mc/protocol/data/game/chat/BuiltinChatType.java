package com.github.steveice10.mc.protocol.data.game.chat;

import com.github.steveice10.mc.protocol.data.game.Identifier;
import lombok.Getter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
public enum BuiltinChatType {
    CHAT,
    SAY_COMMAND,
    MSG_COMMAND_INCOMING,
    MSG_COMMAND_OUTGOING,
    TEAM_MSG_COMMAND_INCOMING,
    TEAM_MSG_COMMAND_OUTGOING,
    EMOTE_COMMAND;

    private static final Map<String, BuiltinChatType> VALUES = new HashMap<>();

    static {
        for (BuiltinChatType type : values()) {
            VALUES.put(type.getResourceLocation(), type);
        }
    }

    private final String resourceLocation = Identifier.formalize(name().toLowerCase(Locale.ROOT));

    public static BuiltinChatType from(String resourceLocation) {
        return VALUES.get(resourceLocation);
    }
}

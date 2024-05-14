package org.geysermc.mcprotocollib.protocol.data.game.chat;

import org.geysermc.mcprotocollib.protocol.data.game.ResourceLocation;

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

    private final ResourceLocation resourceLocation;

    BuiltinChatType() {
        this.resourceLocation = ResourceLocation.fromString(name().toLowerCase(Locale.ROOT));
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    private static final Map<ResourceLocation, BuiltinChatType> VALUES = new HashMap<>();

    public static BuiltinChatType from(ResourceLocation resourceLocation) {
        return VALUES.get(resourceLocation);
    }

    static {
        for (BuiltinChatType type : values()) {
            VALUES.put(type.getResourceLocation(), type);
        }
    }
}

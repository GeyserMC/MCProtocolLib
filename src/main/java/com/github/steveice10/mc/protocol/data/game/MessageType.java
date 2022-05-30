package com.github.steveice10.mc.protocol.data.game;

public enum MessageType {
    CHAT,
    SYSTEM,
    GAME_INFO,
    SAY_COMMAND,
    MSG_COMMAND,
    TEAM_MSG_COMMAND,
    EMOTE_COMMAND,
    TELLRAW_COMMAND;

    private static final MessageType[] VALUES = values();

    public static MessageType from(int id) {
        return VALUES[id];
    }
}

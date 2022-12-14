package com.github.steveice10.mc.protocol.data.game;

public enum PlayerListEntryAction {
    ADD_PLAYER,
    INITIALIZE_CHAT,
    UPDATE_GAME_MODE,
    UPDATE_LISTED,
    UPDATE_LATENCY,
    UPDATE_DISPLAY_NAME;

    public static final PlayerListEntryAction[] VALUES = values();

    public static PlayerListEntryAction from(int id) {
        return VALUES[id];
    }
}

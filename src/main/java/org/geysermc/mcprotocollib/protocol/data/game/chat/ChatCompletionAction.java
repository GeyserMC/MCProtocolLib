package org.geysermc.mcprotocollib.protocol.data.game.chat;

public enum ChatCompletionAction {
    ADD,
    REMOVE,
    SET;

    public static final ChatCompletionAction[] VALUES = values();

    public static ChatCompletionAction from(int id) {
        return VALUES[id];
    }
}

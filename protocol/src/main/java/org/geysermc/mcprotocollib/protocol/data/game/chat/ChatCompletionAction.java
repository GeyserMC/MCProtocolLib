package org.geysermc.mcprotocollib.protocol.data.game.chat;

public enum ChatCompletionAction {
    ADD,
    REMOVE,
    SET;

    public static final ChatCompletionAction[] VALUES = values();

    public static ChatCompletionAction from(int id) {
        return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
    }
}

package com.github.steveice10.mc.protocol.data.game.chat;

public enum ChatCompletionAction {
    ADD,
    REMOVE,
    SET;

    public static final ChatCompletionAction[] VALUES = values();

    public static ChatCompletionAction from(int id) {
        return VALUES[id];
    }
}

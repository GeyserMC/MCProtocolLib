package com.github.steveice10.mc.protocol.data.game.entity.player;

public enum PlayerAction {
    START_DIGGING,
    CANCEL_DIGGING,
    FINISH_DIGGING,
    DROP_ITEM_STACK,
    DROP_ITEM,
    RELEASE_USE_ITEM,
    SWAP_HANDS;

    private static final PlayerAction[] VALUES = values();

    public static PlayerAction from(int id) {
        return VALUES[id];
    }
}

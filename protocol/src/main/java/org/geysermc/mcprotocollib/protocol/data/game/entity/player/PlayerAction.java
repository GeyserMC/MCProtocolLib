package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

public enum PlayerAction {
    START_DIGGING,
    CHANGE_DIGGING_DIRECTION,
    CANCEL_DIGGING,
    FINISH_DIGGING,
    DROP_ITEM_STACK,
    DROP_ITEM,
    RELEASE_USE_ITEM,
    SWAP_HANDS,
    STAB;

    private static final PlayerAction[] VALUES = values();

    public static PlayerAction from(int id) {
        return VALUES[id];
    }
}

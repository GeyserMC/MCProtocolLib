package com.github.steveice10.mc.protocol.data.game.inventory;

public enum ContainerActionType {
    CLICK_ITEM,
    SHIFT_CLICK_ITEM,
    MOVE_TO_HOTBAR_SLOT,
    CREATIVE_GRAB_MAX_STACK,
    DROP_ITEM,
    SPREAD_ITEM,
    FILL_STACK;

    private static final ContainerActionType[] VALUES = values();

    public static ContainerActionType from(int id) {
        return VALUES[id];
    }
}

package com.github.steveice10.mc.protocol.data.game.inventory;

public enum DropItemAction implements ContainerAction {
    DROP_FROM_SELECTED,
    DROP_SELECTED_STACK,
    LEFT_CLICK_OUTSIDE_NOT_HOLDING,
    RIGHT_CLICK_OUTSIDE_NOT_HOLDING;

    public int getId() {
        return this.ordinal();
    }

    private static final DropItemAction[] VALUES = values();

    public static DropItemAction from(int id) {
        return VALUES[id];
    }
}

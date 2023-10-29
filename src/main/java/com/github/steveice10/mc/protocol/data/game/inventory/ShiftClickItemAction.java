package com.github.steveice10.mc.protocol.data.game.inventory;

public enum ShiftClickItemAction implements ContainerAction {
    LEFT_CLICK,
    RIGHT_CLICK;

    private static final ShiftClickItemAction[] VALUES = values();

    public static ShiftClickItemAction from(int id) {
        return VALUES[id];
    }

    public int getId() {
        return this.ordinal();
    }
}

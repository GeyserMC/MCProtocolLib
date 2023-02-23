package com.github.steveice10.mc.protocol.data.game.inventory;

public enum ClickItemAction implements ContainerAction {
    LEFT_CLICK,
    RIGHT_CLICK;

    public int getId() {
        return this.ordinal();
    }

    private static final ClickItemAction[] VALUES = values();

    public static ClickItemAction from(int id) {
        return VALUES[id];
    }
}

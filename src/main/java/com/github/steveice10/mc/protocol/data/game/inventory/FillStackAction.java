package com.github.steveice10.mc.protocol.data.game.inventory;

public enum FillStackAction implements ContainerAction {
    FILL;

    public int getId() {
        return this.ordinal();
    }

    private static final FillStackAction[] VALUES = values();

    public static FillStackAction from(int id) {
        return VALUES[id];
    }
}

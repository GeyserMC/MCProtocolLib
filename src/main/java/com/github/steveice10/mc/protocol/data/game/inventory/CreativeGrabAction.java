package com.github.steveice10.mc.protocol.data.game.inventory;

public enum CreativeGrabAction implements ContainerAction {
    GRAB;

    public int getId() {
        return this.ordinal() + 2;
    }

    private static final CreativeGrabAction[] VALUES = values();

    public static CreativeGrabAction from(int id) {
        return VALUES[id - 2];
    }
}

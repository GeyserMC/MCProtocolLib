package org.geysermc.mcprotocollib.protocol.data.game.inventory;

public enum CreativeGrabAction implements ContainerAction {
    GRAB;

    private static final CreativeGrabAction[] VALUES = values();

    public static CreativeGrabAction from(int id) {
        return VALUES[id - 2];
    }

    public int getId() {
        return this.ordinal() + 2;
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.inventory;

public enum FillStackAction implements ContainerAction {
    FILL;

    private static final FillStackAction[] VALUES = values();

    public static FillStackAction from(int id) {
        return VALUES[id];
    }

    public int getId() {
        return this.ordinal();
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.inventory;

public enum ClickItemAction implements ContainerAction {
    LEFT_CLICK,
    RIGHT_CLICK;

    private static final ClickItemAction[] VALUES = values();

    public static ClickItemAction from(int id) {
        return VALUES[id];
    }

    public int getId() {
        return this.ordinal();
    }
}

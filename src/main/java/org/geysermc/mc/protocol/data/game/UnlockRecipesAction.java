package org.geysermc.mc.protocol.data.game;

public enum UnlockRecipesAction {
    INIT,
    ADD,
    REMOVE;

    private static final UnlockRecipesAction[] VALUES = values();

    public static UnlockRecipesAction from(int id) {
        return VALUES[id];
    }
}

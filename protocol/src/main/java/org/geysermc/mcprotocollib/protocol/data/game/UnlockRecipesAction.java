package org.geysermc.mcprotocollib.protocol.data.game;

public enum UnlockRecipesAction {
    INIT,
    ADD,
    REMOVE;

    private static final UnlockRecipesAction[] VALUES = values();

    public static UnlockRecipesAction from(int id) {
        return VALUES[id];
    }
}

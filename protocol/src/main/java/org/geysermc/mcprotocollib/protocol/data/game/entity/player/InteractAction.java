package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

public enum InteractAction {
    INTERACT,
    ATTACK,
    INTERACT_AT;

    private static final InteractAction[] VALUES = values();

    public static InteractAction from(int id) {
        return VALUES[id];
    }
}

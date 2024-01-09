package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

public enum PositionElement {
    X,
    Y,
    Z,
    PITCH,
    YAW;

    private static final PositionElement[] VALUES = values();

    public static PositionElement from(int id) {
        return VALUES[id];
    }
}

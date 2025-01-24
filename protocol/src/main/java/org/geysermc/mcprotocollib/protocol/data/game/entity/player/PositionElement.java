package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

public enum PositionElement {
    X,
    Y,
    Z,
    Y_ROT,
    X_ROT,
    DELTA_X,
    DELTA_Y,
    DELTA_Z,
    ROTATE_DELTA;

    private static final PositionElement[] VALUES = values();

    public static PositionElement from(int id) {
        return VALUES[id];
    }
}

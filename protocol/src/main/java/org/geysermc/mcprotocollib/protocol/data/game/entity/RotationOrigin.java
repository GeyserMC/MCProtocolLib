package org.geysermc.mcprotocollib.protocol.data.game.entity;

public enum RotationOrigin {
    FEET,
    EYES;

    private static final RotationOrigin[] VALUES = values();

    public static RotationOrigin from(int id) {
        return VALUES[id];
    }
}

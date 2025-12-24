package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

public enum HumanoidArm {
    LEFT,
    RIGHT;

    private static final HumanoidArm[] VALUES = values();

    public static HumanoidArm from(int id) {
        return id >= 0 & id < VALUES.length ? VALUES[id] : VALUES[0];
    }
}

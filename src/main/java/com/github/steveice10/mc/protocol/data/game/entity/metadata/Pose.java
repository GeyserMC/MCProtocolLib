package com.github.steveice10.mc.protocol.data.game.entity.metadata;

public enum Pose {
    STANDING,
    FALL_FLYING,
    SLEEPING,
    SWIMMING,
    SPIN_ATTACK,
    SNEAKING,
    LONG_JUMPING,
    DYING,
    CROAKING,
    USING_TONGUE,
    SITTING,
    ROARING,
    SNIFFING,
    EMERGING,
    DIGGING,
    SLIDING,
    SHOOTING,
    INHALING;

    private static final Pose[] VALUES = values();

    public static Pose from(int id) {
        return VALUES[id];
    }
}

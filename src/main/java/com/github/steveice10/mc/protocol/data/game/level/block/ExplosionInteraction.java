package com.github.steveice10.mc.protocol.data.game.level.block;

public enum ExplosionInteraction {
    KEEP,
    DESTROY,
    DESTROY_WITH_DECAY,
    TRIGGER_BLOCK;

    private static final ExplosionInteraction[] VALUES = values();

    public static ExplosionInteraction from(int id) {
        return VALUES[id];
    }
}

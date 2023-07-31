package com.github.steveice10.mc.protocol.data.game.entity.player;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

public enum Animation {
    SWING_ARM(0),
    LEAVE_BED(2),
    SWING_OFFHAND(3),
    CRITICAL_HIT(4),
    ENCHANTMENT_CRITICAL_HIT(5);

    private final int id;

    Animation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static final Int2ObjectMap<Animation> VALUES = new Int2ObjectOpenHashMap<>();

    @Nullable
    public static Animation from(int id) {
        return VALUES.get(id);
    }

    static {
        for (Animation animation : values()) {
            VALUES.put(animation.id, animation);
        }
    }
}

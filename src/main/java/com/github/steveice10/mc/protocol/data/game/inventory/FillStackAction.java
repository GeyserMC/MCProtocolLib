package com.github.steveice10.mc.protocol.data.game.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum FillStackAction implements ContainerAction {
    FILL(0);

    private final int id;

    FillStackAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static Int2ObjectMap<FillStackAction> VALUES = new Int2ObjectOpenHashMap<>();

    public static FillStackAction from(int id) {
        return VALUES.get(id);
    }

    static {
        for (FillStackAction action : values()) {
            VALUES.put(action.id, action);
        }
    }
}

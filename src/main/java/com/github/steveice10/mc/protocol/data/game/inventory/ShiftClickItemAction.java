package com.github.steveice10.mc.protocol.data.game.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum ShiftClickItemAction implements ContainerAction {
    LEFT_CLICK(0),
    RIGHT_CLICK(1);

    private final int id;

    ShiftClickItemAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static Int2ObjectMap<ShiftClickItemAction> VALUES = new Int2ObjectOpenHashMap<>();

    public static ShiftClickItemAction from(int id) {
        return VALUES.get(id);
    }

    static {
        for (ShiftClickItemAction action : values()) {
            VALUES.put(action.id, action);
        }
    }
}

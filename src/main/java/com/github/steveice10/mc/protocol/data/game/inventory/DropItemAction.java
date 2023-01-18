package com.github.steveice10.mc.protocol.data.game.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum DropItemAction implements ContainerAction {
    DROP_FROM_SELECTED(0),
    DROP_SELECTED_STACK(1),
    LEFT_CLICK_OUTSIDE_NOT_HOLDING(2),
    RIGHT_CLICK_OUTSIDE_NOT_HOLDING(3);

    private final int id;

    DropItemAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static Int2ObjectMap<DropItemAction> VALUES = new Int2ObjectOpenHashMap<>();

    public static DropItemAction from(int id) {
        return VALUES.get(id);
    }

    static {
        for (DropItemAction action : values()) {
            VALUES.put(action.id, action);
        }
    }
}

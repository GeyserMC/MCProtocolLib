package com.github.steveice10.mc.protocol.data.game.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum ClickItemAction implements ContainerAction {
    LEFT_CLICK(0),
    RIGHT_CLICK(1);

    private final int id;

    ClickItemAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static Int2ObjectMap<ClickItemAction> VALUES = new Int2ObjectOpenHashMap<>();

    public static ClickItemAction from(int id) {
        return VALUES.get(id);
    }

    static {
        for (ClickItemAction action : values()) {
            VALUES.put(action.id, action);
        }
    }
}

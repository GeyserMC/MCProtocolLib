package com.github.steveice10.mc.protocol.data.game.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum CreativeGrabAction implements ContainerAction {
    GRAB(2);

    private final int id;

    CreativeGrabAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static Int2ObjectMap<CreativeGrabAction> VALUES = new Int2ObjectOpenHashMap<>();

    public static CreativeGrabAction from(int id) {
        return VALUES.get(id);
    }

    static {
        for (CreativeGrabAction action : values()) {
            VALUES.put(action.id, action);
        }
    }
}

package com.github.steveice10.mc.protocol.data.game.level.notify;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum DemoMessageValue implements GameEventValue {
    WELCOME(0),
    MOVEMENT_CONTROLS(101),
    JUMP_CONTROL(102),
    INVENTORY_CONTROL(103),
    SCREENSHOT_CONTROL(104);

    private final int id;

    DemoMessageValue(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    private static Int2ObjectMap<DemoMessageValue> VALUES = new Int2ObjectOpenHashMap<>();

    public static DemoMessageValue from(int id) {
        return VALUES.get(id);
    }

    static {
        for (DemoMessageValue value : values()) {
            VALUES.put(value.id, value);
        }
    }
}

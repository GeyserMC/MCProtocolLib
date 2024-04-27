package org.geysermc.mcprotocollib.protocol.data.game.level.notify;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum DemoMessageValue implements GameEventValue {
    WELCOME(0),
    MOVEMENT_CONTROLS(101),
    JUMP_CONTROL(102),
    INVENTORY_CONTROL(103),
    SCREENSHOT_CONTROL(104);

    private static final Int2ObjectMap<DemoMessageValue> VALUES = new Int2ObjectOpenHashMap<>();

    static {
        for (DemoMessageValue value : values()) {
            VALUES.put(value.id, value);
        }
    }

    private final int id;

    DemoMessageValue(int id) {
        this.id = id;
    }

    public static DemoMessageValue from(int id) {
        return VALUES.get(id);
    }

    public int getId() {
        return this.id;
    }
}

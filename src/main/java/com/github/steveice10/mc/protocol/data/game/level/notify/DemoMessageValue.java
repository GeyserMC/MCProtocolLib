package com.github.steveice10.mc.protocol.data.game.level.notify;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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

    public static DemoMessageValue from(int id) {
        return VALUES.get(id);
    }
}

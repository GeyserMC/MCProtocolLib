package com.github.steveice10.mc.protocol.data.game.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum MoveToHotbarAction implements ContainerAction {
    SLOT_1(0),
    SLOT_2(1),
    SLOT_3(2),
    SLOT_4(3),
    SLOT_5(4),
    SLOT_6(5),
    SLOT_7(6),
    SLOT_8(7),
    SLOT_9(8),
    OFF_HAND(40);

    private final int id;

    MoveToHotbarAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static Int2ObjectMap<MoveToHotbarAction> VALUES = new Int2ObjectOpenHashMap<>();

    public static MoveToHotbarAction from(int id) {
        return VALUES.get(id);
    }

    static {
        for (MoveToHotbarAction action : values()) {
            VALUES.putIfAbsent(action.id, action);
        }
    }
}

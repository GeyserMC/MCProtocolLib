package com.github.steveice10.mc.protocol.data.game.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpreadItemAction implements ContainerAction {
    LEFT_MOUSE_BEGIN_DRAG(0),
    LEFT_MOUSE_ADD_SLOT(1),
    LEFT_MOUSE_END_DRAG(2),
    RIGHT_MOUSE_BEGIN_DRAG(4),
    RIGHT_MOUSE_ADD_SLOT(5),
    RIGHT_MOUSE_END_DRAG(6),
    MIDDLE_MOUSE_BEGIN_DRAG(8),
    MIDDLE_MOUSE_ADD_SLOT(9),
    MIDDLE_MOUSE_END_DRAG(10);

    private static final Int2ObjectMap<SpreadItemAction> VALUES = new Int2ObjectOpenHashMap<>();

    static {
        for (SpreadItemAction action : values()) {
            VALUES.put(action.id, action);
        }
    }

    private final int id;

    public static SpreadItemAction from(int id) {
        return VALUES.get(id);
    }
}

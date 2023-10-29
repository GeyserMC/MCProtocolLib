package com.github.steveice10.mc.protocol.data.game.entity.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Direction implements ObjectData {
    DOWN(-1),
    UP(-1),
    NORTH(0),
    SOUTH(1),
    WEST(2),
    EAST(3);

    public static final Direction[] VALUES = values();
    private static final Direction[] HORIZONTAL_VALUES = {NORTH, SOUTH, WEST, EAST};
    private final int horizontalIndex;

    public static Direction getByHorizontalIndex(int index) {
        return HORIZONTAL_VALUES[index % HORIZONTAL_VALUES.length];
    }

    public static Direction from(int id) {
        return VALUES[id];
    }
}

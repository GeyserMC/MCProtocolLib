package com.github.steveice10.mc.protocol.data.game.level.event;

import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;

import java.util.EnumSet;
import java.util.Set;

public record SculkBlockChargeEventData(int charge, Set<Direction> blockFaces) implements LevelEventData {
    public SculkBlockChargeEventData(int value) {
        this(value >> 6, EnumSet.noneOf(Direction.class));

        int blockFaceBits = value & 63;
        for (Direction direction : Direction.VALUES) {
            if (((blockFaceBits >> direction.ordinal()) & 1) == 1) {
                blockFaces.add(direction);
            }
        }
    }

    public int getLevelValue() {
        int value = this.charge << 6;
        for (Direction direction : blockFaces) {
            value |= (1 << direction.ordinal());
        }

        return value;
    }
}

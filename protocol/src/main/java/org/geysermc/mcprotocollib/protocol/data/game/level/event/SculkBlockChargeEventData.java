package org.geysermc.mcprotocollib.protocol.data.game.level.event;

import lombok.Value;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;

import java.util.EnumSet;
import java.util.Set;

@Value
public class SculkBlockChargeEventData implements LevelEventData {
    int charge;
    Set<Direction> blockFaces;

    public SculkBlockChargeEventData(int charge, Set<Direction> blockFaces) {
        this.charge = charge;
        this.blockFaces = blockFaces;
    }

    public SculkBlockChargeEventData(int value) {
        this.charge = value >> 6;
        this.blockFaces = EnumSet.noneOf(Direction.class);

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

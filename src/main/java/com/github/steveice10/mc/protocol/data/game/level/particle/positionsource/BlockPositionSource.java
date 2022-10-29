package com.github.steveice10.mc.protocol.data.game.level.particle.positionsource;

import lombok.Value;
import org.cloudburstmc.math.vector.Vector3i;

@Value
public class BlockPositionSource implements PositionSource {
    Vector3i position;

    @Override
    public PositionSourceType getType() {
        return PositionSourceType.BLOCK;
    }
}

package com.github.steveice10.mc.protocol.data.game.level.particle.positionsource;

import org.cloudburstmc.math.vector.Vector3i;

public record BlockPositionSource(Vector3i position) implements PositionSource {
    @Override
    public PositionSourceType getType() {
        return PositionSourceType.BLOCK;
    }
}

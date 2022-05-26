package com.github.steveice10.mc.protocol.data.game.level.particle.positionsource;

import lombok.Value;

@Value
public class EntityPositionSource implements PositionSource {
    int entityId;
    float yOffset;

    @Override
    public PositionSourceType getType() {
        return PositionSourceType.ENTITY;
    }
}

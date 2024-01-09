package org.geysermc.mcprotocollib.protocol.data.game.level.particle.positionsource;

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

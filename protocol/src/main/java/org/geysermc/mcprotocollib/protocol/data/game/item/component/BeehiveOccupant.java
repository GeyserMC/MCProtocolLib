package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class BeehiveOccupant {
    private final TypedEntityData<EntityType> entityData;
    private final int ticksInHive;
    private final int minTicksInHive;
}

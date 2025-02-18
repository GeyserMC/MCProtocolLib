package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.cloudburstmc.nbt.NbtMap;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class BeehiveOccupant {
    private final NbtMap entityData;
    private final int ticksInHive;
    private final int minTicksInHive;
}

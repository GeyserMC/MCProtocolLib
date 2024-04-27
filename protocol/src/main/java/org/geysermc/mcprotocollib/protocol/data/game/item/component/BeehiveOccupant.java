package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cloudburstmc.nbt.NbtMap;

@Data
@AllArgsConstructor
public class BeehiveOccupant {
    private final NbtMap entityData;
    private final int ticksInHive;
    private final int minTicksInHive;
}

package org.geysermc.mcprotocollib.protocol.data.game.level.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BonemealGrowEventData implements LevelEventData {
    private final int particleCount;
}

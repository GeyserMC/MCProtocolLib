package org.geysermc.mcprotocollib.protocol.data.game.level.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;

@Data
@AllArgsConstructor
public class ElectricSparkData implements LevelEventData {
    private final Direction direction;
}

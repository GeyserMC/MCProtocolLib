package org.geysermc.mcprotocollib.protocol.data.game.level.event;

import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmokeEventData implements LevelEventData {
    private final Direction direction;
}

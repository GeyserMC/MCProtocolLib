package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PistonValue implements BlockValue {
    private final Direction direction;
}

package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;

@Data
@AllArgsConstructor
public class BellValue implements BlockValue {
    private final Direction direction;
}

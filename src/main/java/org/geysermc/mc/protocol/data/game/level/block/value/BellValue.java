package org.geysermc.mc.protocol.data.game.level.block.value;

import org.geysermc.mc.protocol.data.game.entity.object.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BellValue implements BlockValue {
    private final Direction direction;
}

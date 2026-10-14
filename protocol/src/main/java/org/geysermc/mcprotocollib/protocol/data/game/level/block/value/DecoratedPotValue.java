package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.WobbleStyle;

@Data
@AllArgsConstructor
public class DecoratedPotValue implements BlockValue {
    private final WobbleStyle wobbleStyle;
}

package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

import org.geysermc.mcprotocollib.protocol.data.game.level.block.WobbleStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class DecoratedPotValue implements BlockValue {
    @NonNull
    private final WobbleStyle wobbleStyle;
}

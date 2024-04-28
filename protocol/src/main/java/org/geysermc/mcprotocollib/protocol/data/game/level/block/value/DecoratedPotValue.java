package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.WobbleStyle;

@Data
@AllArgsConstructor
public class DecoratedPotValue implements BlockValue {
    @NonNull
    private final WobbleStyle wobbleStyle;
}

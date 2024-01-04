package org.geysermc.mc.protocol.data.game.level.block.value;

import org.geysermc.mc.protocol.data.game.level.block.WobbleStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class DecoratedPotValue implements BlockValue {
    @NonNull
    private final WobbleStyle wobbleStyle;
}

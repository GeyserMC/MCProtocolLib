package com.github.steveice10.mc.protocol.data.game.level.block.value;

import com.github.steveice10.mc.protocol.data.game.level.block.WobbleStyle;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecoratedPotValue implements BlockValue {
    private final WobbleStyle wobbleStyle;
}

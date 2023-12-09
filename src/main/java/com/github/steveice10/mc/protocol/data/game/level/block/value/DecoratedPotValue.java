package com.github.steveice10.mc.protocol.data.game.level.block.value;

import com.github.steveice10.mc.protocol.data.game.level.block.WobbleStyle;
import lombok.NonNull;

public record DecoratedPotValue(@NonNull WobbleStyle wobbleStyle) implements BlockValue {
}

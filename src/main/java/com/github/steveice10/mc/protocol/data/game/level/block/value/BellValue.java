package com.github.steveice10.mc.protocol.data.game.level.block.value;

import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;

public record BellValue(Direction direction) implements BlockValue {
}

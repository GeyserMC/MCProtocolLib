package com.github.steveice10.mc.protocol.data.game.level.event;

import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;

public record ElectricSparkData(Direction direction) implements LevelEventData {
}

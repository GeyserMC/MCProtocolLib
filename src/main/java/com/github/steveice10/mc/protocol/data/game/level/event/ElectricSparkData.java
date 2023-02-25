package com.github.steveice10.mc.protocol.data.game.level.event;

import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElectricSparkData implements LevelEventData {
    private final Direction direction;
}

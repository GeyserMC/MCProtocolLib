package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;

public record KillEntityStatistic(EntityType entity) implements Statistic {
}

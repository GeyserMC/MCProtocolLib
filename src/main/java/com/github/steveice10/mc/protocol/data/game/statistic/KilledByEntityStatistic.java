package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;

public record KilledByEntityStatistic(EntityType entity) implements Statistic {
}

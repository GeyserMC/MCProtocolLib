package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KilledByEntityStatistic implements Statistic {
    private final EntityType entity;
}

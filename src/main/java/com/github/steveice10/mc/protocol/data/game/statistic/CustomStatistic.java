package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.data.MagicValues;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomStatistic implements Statistic {
    private final int category;
    private final int id;

    public CustomStatistic(int id) {
        this(MagicValues.value(Integer.class, StatisticCategory.GENERIC), id);
    }
}

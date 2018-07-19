package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class CustomStatistic implements Statistic {
    private int category;
    private int id;

    public CustomStatistic(int id) {
        this(MagicValues.value(Integer.class, StatisticCategory.GENERIC), id);
    }

    public CustomStatistic(int category, int id) {
        this.category = category;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public int getCategory() {
        return this.category;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof CustomStatistic)) return false;

        CustomStatistic that = (CustomStatistic) o;
        return this.category == that.category
                && this.id == that.id;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.id, this.category);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

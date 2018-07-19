package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class UseItemStatistic implements Statistic {
    private int id;

    public UseItemStatistic(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof UseItemStatistic)) return false;

        UseItemStatistic that = (UseItemStatistic) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.id);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class PickupItemStatistic implements Statistic {
    private int id;

    public PickupItemStatistic(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof PickupItemStatistic)) return false;

        PickupItemStatistic that = (PickupItemStatistic) o;
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

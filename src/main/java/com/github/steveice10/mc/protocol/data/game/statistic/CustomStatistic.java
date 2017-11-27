package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

import java.util.Objects;

public class CustomStatistic implements Statistic {
    private String name;

    public CustomStatistic(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof CustomStatistic)) return false;

        CustomStatistic that = (CustomStatistic) o;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.name);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

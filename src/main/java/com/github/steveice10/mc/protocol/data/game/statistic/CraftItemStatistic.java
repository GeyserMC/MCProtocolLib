package com.github.steveice10.mc.protocol.data.game.statistic;

public class CraftItemStatistic implements Statistic {

    private int id;

    public CraftItemStatistic(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        CraftItemStatistic that = (CraftItemStatistic) o;

        if(id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

}

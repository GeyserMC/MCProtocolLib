package com.github.steveice10.mc.protocol.data.game.statistic;

public class PickupItemStatistic implements Statistic {

    private String id;

    public PickupItemStatistic(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        PickupItemStatistic that = (PickupItemStatistic) o;

        if(!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}

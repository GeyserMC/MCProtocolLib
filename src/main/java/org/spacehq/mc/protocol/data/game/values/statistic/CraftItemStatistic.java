package org.spacehq.mc.protocol.data.game.values.statistic;

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

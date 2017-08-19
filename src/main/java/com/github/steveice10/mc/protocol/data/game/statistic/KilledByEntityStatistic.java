package com.github.steveice10.mc.protocol.data.game.statistic;

public class KilledByEntityStatistic implements Statistic {

    private String id;

    public KilledByEntityStatistic(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        KilledByEntityStatistic that = (KilledByEntityStatistic) o;

        if(!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}

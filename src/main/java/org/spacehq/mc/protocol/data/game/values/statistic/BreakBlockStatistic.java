package org.spacehq.mc.protocol.data.game.values.statistic;

public class BreakBlockStatistic implements Statistic {

    private int id;

    public BreakBlockStatistic(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}

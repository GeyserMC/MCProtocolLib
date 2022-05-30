package com.github.steveice10.mc.protocol.data.game.statistic;

public enum StatisticCategory {
    BREAK_BLOCK,
    CRAFT_ITEM,
    USE_ITEM,
    BREAK_ITEM,
    PICKED_UP_ITEM,
    DROP_ITEM,
    KILL_ENTITY,
    KILLED_BY_ENTITY,
    CUSTOM;

    private static final StatisticCategory[] VALUES = values();

    public static StatisticCategory from(int id) {
        return VALUES[id];
    }
}

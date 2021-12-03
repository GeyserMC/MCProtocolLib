package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.packetlib.io.NetInput;

import java.io.IOException;

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

    public static StatisticCategory read(NetInput in) throws IOException {
        return in.readEnum(VALUES);
    }

    public static StatisticCategory fromId(int id) {
        return VALUES[id];
    }
}

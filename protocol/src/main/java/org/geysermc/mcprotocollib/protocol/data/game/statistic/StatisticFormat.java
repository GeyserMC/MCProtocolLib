package org.geysermc.mcprotocollib.protocol.data.game.statistic;

public enum StatisticFormat {
    /**
     * Distance in centimeters
     */
    DISTANCE,

    /**
     * Time in ticks
     */
    TIME,

    INTEGER,

    /**
     * Used for damage statistics.
     */
    TENTHS
}

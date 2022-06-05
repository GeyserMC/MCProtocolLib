package com.github.steveice10.mc.protocol.data.game.entity.player;

public enum BlockBreakStage {
    STAGE_1,
    STAGE_2,
    STAGE_3,
    STAGE_4,
    STAGE_5,
    STAGE_6,
    STAGE_7,
    STAGE_8,
    STAGE_9,
    STAGE_10,
    RESET;

    /**
     * All stages, excluding reset
     */
    public static final BlockBreakStage[] STAGES;

    static {
        BlockBreakStage[] allValues = values();
        STAGES = new BlockBreakStage[allValues.length - 1];
        System.arraycopy(allValues, 0, STAGES, 0, 10);
    }
}

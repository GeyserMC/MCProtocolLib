package com.github.steveice10.mc.protocol.data.game.statistic;

import lombok.Getter;

/**
 * "Custom" statistics in Minecraft that don't belong to any
 * specific category.
 */
public enum CustomStatistic implements Statistic {
    LEAVE_GAME,
    PLAY_TIME(StatisticFormat.TIME),
    TOTAL_WORLD_TIME(StatisticFormat.TIME),
    TIME_SINCE_DEATH(StatisticFormat.TIME),
    TIME_SINCE_REST(StatisticFormat.TIME),
    SNEAK_TIME(StatisticFormat.TIME),
    WALK_ONE_CM(StatisticFormat.DISTANCE),
    CROUCH_ONE_CM(StatisticFormat.DISTANCE),
    SPRINT_ONE_CM(StatisticFormat.DISTANCE),
    WALK_ON_WATER_ONE_CM(StatisticFormat.DISTANCE),
    FALL_ONE_CM(StatisticFormat.DISTANCE),
    CLIMB_ONE_CM(StatisticFormat.DISTANCE),
    FLY_ONE_CM(StatisticFormat.DISTANCE),
    WALK_UNDER_WATER_ONE_CM(StatisticFormat.DISTANCE),
    MINECART_ONE_CM(StatisticFormat.DISTANCE),
    BOAT_ONE_CM(StatisticFormat.DISTANCE),
    PIG_ONE_CM(StatisticFormat.DISTANCE),
    HORSE_ONE_CM(StatisticFormat.DISTANCE),
    AVIATE_ONE_CM(StatisticFormat.DISTANCE),
    SWIM_ONE_CM(StatisticFormat.DISTANCE),
    STRIDER_ONE_CM(StatisticFormat.DISTANCE),
    JUMP,
    DROP,
    DAMAGE_DEALT(StatisticFormat.TENTHS),
    DAMAGE_DEALT_ABSORBED(StatisticFormat.TENTHS),
    DAMAGE_DEALT_RESISTED(StatisticFormat.TENTHS),
    DAMAGE_TAKEN(StatisticFormat.TENTHS),
    DAMAGE_BLOCKED_BY_SHIELD(StatisticFormat.TENTHS),
    DAMAGE_ABSORBED(StatisticFormat.TENTHS),
    DAMAGE_RESISTED(StatisticFormat.TENTHS),
    DEATHS,
    MOB_KILLS,
    ANIMALS_BRED,
    PLAYER_KILLS,
    FISH_CAUGHT,
    TALKED_TO_VILLAGER,
    TRADED_WITH_VILLAGER,
    EAT_CAKE_SLICE,
    FILL_CAULDRON,
    USE_CAULDRON,
    CLEAN_ARMOR,
    CLEAN_BANNER,
    CLEAN_SHULKER_BOX,
    INTERACT_WITH_BREWINGSTAND,
    INTERACT_WITH_BEACON,
    INSPECT_DROPPER,
    INSPECT_HOPPER,
    INSPECT_DISPENSER,
    PLAY_NOTEBLOCK,
    TUNE_NOTEBLOCK,
    POT_FLOWER,
    TRIGGER_TRAPPED_CHEST,
    OPEN_ENDERCHEST,
    ENCHANT_ITEM,
    PLAY_RECORD,
    INTERACT_WITH_FURNACE,
    INTERACT_WITH_CRAFTING_TABLE,
    OPEN_CHEST,
    SLEEP_IN_BED,
    OPEN_SHULKER_BOX,
    OPEN_BARREL,
    INTERACT_WITH_BLAST_FURNACE,
    INTERACT_WITH_SMOKER,
    INTERACT_WITH_LECTERN,
    INTERACT_WITH_CAMPFIRE,
    INTERACT_WITH_CARTOGRAPHY_TABLE,
    INTERACT_WITH_LOOM,
    INTERACT_WITH_STONECUTTER,
    BELL_RING,
    RAID_TRIGGER,
    RAID_WIN,
    INTERACT_WITH_ANVIL,
    INTERACT_WITH_GRINDSTONE,
    TARGET_HIT,
    INTERACT_WITH_SMITHING_TABLE;

    private static final CustomStatistic[] VALUES = values();

    @Getter
    private final StatisticFormat format;

    CustomStatistic() {
        format = StatisticFormat.INTEGER;
    }

    CustomStatistic(StatisticFormat format) {
        this.format = format;
    }

    public static CustomStatistic from(int id) {
        return VALUES[id];
    }
}

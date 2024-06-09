package org.geysermc.mcprotocollib.protocol.data.game.entity;

public enum Effect {
    SPEED,
    SLOWNESS,
    HASTE,
    MINING_FATIGUE,
    STRENGTH,
    INSTANT_HEALTH,
    INSTANT_DAMAGE,
    JUMP_BOOST,
    NAUSEA,
    REGENERATION,
    RESISTANCE,
    FIRE_RESISTANCE,
    WATER_BREATHING,
    INVISIBILITY,
    BLINDNESS,
    NIGHT_VISION,
    HUNGER,
    WEAKNESS,
    POISON,
    WITHER,
    HEALTH_BOOST,
    ABSORPTION,
    SATURATION,
    GLOWING,
    LEVITATION,
    LUCK,
    UNLUCK,
    SLOW_FALLING,
    CONDUIT_POWER,
    DOLPHINS_GRACE,
    BAD_OMEN,
    HERO_OF_THE_VILLAGE,
    DARKNESS,
    TRIAL_OMEN,
    RAID_OMEN,
    WIND_CHARGED,
    WEAVING,
    OOZING,
    INFESTED;

    public static final Effect[] VALUES = values();

    public static Effect from(int id) {
        return VALUES[id];
    }
}

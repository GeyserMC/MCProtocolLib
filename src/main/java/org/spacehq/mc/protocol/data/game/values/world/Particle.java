package org.spacehq.mc.protocol.data.game.values.world;

public enum Particle {
    EXPLOSION_NORMAL,
    EXPLOSION_LARGE,
    EXPLOSION_HUGE,
    FIREWORKS_SPARK,
    WATER_BUBBLE,
    WATER_SPLASH,
    WATER_WAKE,
    SUSPENDED,
    SUSPENDED_DEPTH,
    CRIT,
    CRIT_MAGIC,
    SMOKE_NORMAL,
    SMOKE_LARGE,
    SPELL,
    SPELL_INSTANT,
    SPELL_MOB,
    SPELL_MOB_AMBIENT,
    SPELL_WITCH,
    DRIP_WATER,
    DRIP_LAVA,
    VILLAGER_ANGRY,
    VILLAGER_HAPPY,
    TOWN_AURA,
    NOTE,
    PORTAL,
    ENCHANTMENT_TABLE,
    FLAME,
    LAVA,
    FOOTSTEP,
    CLOUD,
    REDSTONE,
    SNOWBALL,
    SNOW_SHOVEL,
    SLIME,
    HEART,
    BARRIER,
    ICON_CRACK(2),
    BLOCK_CRACK(1),
    BLOCK_DUST(1),
    WATER_DROP,
    ITEM_TAKE,
    MOB_APPEARANCE;

    private int dataLength;

    private Particle() {
        this(0);
    }

    private Particle(int dataLength) {
        this.dataLength = dataLength;
    }

    public int getDataLength() {
        return this.dataLength;
    }
}

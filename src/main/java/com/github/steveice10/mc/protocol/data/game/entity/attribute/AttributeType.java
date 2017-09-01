package com.github.steveice10.mc.protocol.data.game.entity.attribute;

public enum AttributeType {
    GENERIC_MAX_HEALTH(20, 0, 1024),
    GENERIC_FOLLOW_RANGE(32, 0, 2048),
    GENERIC_KNOCKBACK_RESISTANCE(0, 0, 1),
    GENERIC_MOVEMENT_SPEED(0.699999988079071, 0, 1024),
    GENERIC_ATTACK_DAMAGE(2, 0, 2048),
    GENERIC_ATTACK_SPEED(4, 0, 1024),
    GENERIC_ARMOR(0, 0, 30),
    GENERIC_ARMOR_TOUGHNESS(0, 0, 20),
    GENERIC_LUCK(0, -1024, 1024),
    GENERIC_FLYING_SPEED(0.4000000059604645, 0, 1024),
    HORSE_JUMP_STRENGTH(0.7, 0, 2),
    ZOMBIE_SPAWN_REINFORCEMENTS(0, 0, 1);

    private double def;
    private double min;
    private double max;

    private AttributeType(double def, double min, double max) {
        this.def = def;
        this.min = min;
        this.max = max;
    }

    public double getDefault() {
        return this.def;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }
}

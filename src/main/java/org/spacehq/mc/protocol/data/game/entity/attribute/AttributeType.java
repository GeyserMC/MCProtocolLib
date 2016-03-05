package org.spacehq.mc.protocol.data.game.entity.attribute;

public enum AttributeType {

    MAX_HEALTH(20, 0, 1024),
    FOLLOW_RANGE(32, 0, 2048),
    KNOCKBACK_RESISTANCE(0, 0, 1),
    MOVEMENT_SPEED(0.699999988079071, 0, 1024),
    ATTACK_DAMAGE(2, 0, 2048),
    ATTACK_SPEED(4, 0, 1024),
    ARMOR(0, 0, 30),
    LUCK(0, -1024, 1024),
    HORSE_JUMP_STRENGTH(0.7, 0, 2),
    ZOMBIE_SPAWN_REINFORCEMENTS_CHANCE(0, 0, 1);

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

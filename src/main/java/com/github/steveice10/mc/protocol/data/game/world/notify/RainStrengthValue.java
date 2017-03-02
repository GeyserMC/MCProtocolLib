package com.github.steveice10.mc.protocol.data.game.world.notify;

public class RainStrengthValue implements ClientNotificationValue {
    private float strength;

    public RainStrengthValue(float strength) {
        if(strength > 1) {
            strength = 1;
        }

        if(strength < 0) {
            strength = 0;
        }

        this.strength = strength;
    }

    public float getStrength() {
        return this.strength;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RainStrengthValue && Float.compare(this.strength, ((RainStrengthValue) o).strength) == 0;
    }

    @Override
    public int hashCode() {
        return this.strength != +0.0f ? Float.floatToIntBits(this.strength) : 0;
    }
}

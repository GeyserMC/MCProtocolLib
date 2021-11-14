package com.github.steveice10.mc.protocol.data.game.level.notify;

import lombok.Data;

@Data
public class RainStrengthValue implements GameEventValue {
    private final float strength;

    public RainStrengthValue(float strength) {
        if (strength > 1) {
            strength = 1;
        }

        if (strength < 0) {
            strength = 0;
        }

        this.strength = strength;
    }
}

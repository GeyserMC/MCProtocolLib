package com.github.steveice10.mc.protocol.data.game.world.notify;

import lombok.Data;

@Data
public class ThunderStrengthValue implements ClientNotificationValue {
    private final float strength;

    public ThunderStrengthValue(float strength) {
        if (strength > 1) {
            strength = 1;
        }

        if (strength < 0) {
            strength = 0;
        }

        this.strength = strength;
    }
}

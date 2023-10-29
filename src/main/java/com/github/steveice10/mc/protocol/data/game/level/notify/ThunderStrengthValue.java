package com.github.steveice10.mc.protocol.data.game.level.notify;

public record ThunderStrengthValue(float strength) implements GameEventValue {
    public ThunderStrengthValue {
        if (strength > 1) {
            strength = 1;
        }

        if (strength < 0) {
            strength = 0;
        }

    }
}

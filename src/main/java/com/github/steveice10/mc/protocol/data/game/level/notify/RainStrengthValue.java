package com.github.steveice10.mc.protocol.data.game.level.notify;

public record RainStrengthValue(float strength) implements GameEventValue {
    public RainStrengthValue {
        if (strength > 1) {
            strength = 1;
        }

        if (strength < 0) {
            strength = 0;
        }

    }
}

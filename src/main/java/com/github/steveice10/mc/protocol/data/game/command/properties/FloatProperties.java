package com.github.steveice10.mc.protocol.data.game.command.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FloatProperties implements CommandProperties {
    private final float min;
    private final float max;

    public FloatProperties() {
        this.min = -Float.MAX_VALUE;
        this.max = Float.MAX_VALUE;
    }
}

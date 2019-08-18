package com.github.steveice10.mc.protocol.data.game.command.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoubleProperties implements CommandProperties {
    private final double min;
    private final double max;

    public DoubleProperties() {
        this.min = -Double.MAX_VALUE;
        this.max = Double.MAX_VALUE;
    }
}

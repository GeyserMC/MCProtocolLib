package com.github.steveice10.mc.protocol.data.game.command.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IntegerProperties implements CommandProperties {
    private final int min;
    private final int max;

    public IntegerProperties() {
        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
    }
}

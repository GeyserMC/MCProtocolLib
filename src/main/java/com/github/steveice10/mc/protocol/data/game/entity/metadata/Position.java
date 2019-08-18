package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {
    private final int x;
    private final int y;
    private final int z;
}

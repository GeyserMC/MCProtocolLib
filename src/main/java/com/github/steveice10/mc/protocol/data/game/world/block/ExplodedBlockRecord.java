package com.github.steveice10.mc.protocol.data.game.world.block;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExplodedBlockRecord {
    private final int x;
    private final int y;
    private final int z;
}

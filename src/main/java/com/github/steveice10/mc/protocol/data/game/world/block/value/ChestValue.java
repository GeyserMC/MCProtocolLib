package com.github.steveice10.mc.protocol.data.game.world.block.value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChestValue implements BlockValue {
    private final int viewers;
}

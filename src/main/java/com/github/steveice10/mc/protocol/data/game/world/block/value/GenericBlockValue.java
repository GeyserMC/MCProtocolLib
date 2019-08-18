package com.github.steveice10.mc.protocol.data.game.world.block.value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericBlockValue implements BlockValue {
    private final int value;
}

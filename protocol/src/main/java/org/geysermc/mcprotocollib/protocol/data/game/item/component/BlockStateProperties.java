package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class BlockStateProperties {
    private final Map<String, String> properties;
}

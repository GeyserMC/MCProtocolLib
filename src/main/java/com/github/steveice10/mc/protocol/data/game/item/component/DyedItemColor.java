package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DyedItemColor {
    private final int rgb;
    private final boolean showInTooltip;
}

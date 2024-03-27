package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BannerPatternLayer {
    private final int patternId;
    private final int colorId;
}

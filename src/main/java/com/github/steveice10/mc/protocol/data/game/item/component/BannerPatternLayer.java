package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class BannerPatternLayer {
    private final int patternId;
    private final @Nullable String assetId;
    private final @Nullable String translationKey;
    private final int colorId;
}

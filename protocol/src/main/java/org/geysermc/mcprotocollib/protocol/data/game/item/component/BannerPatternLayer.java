package com.github.steveice10.mc.protocol.data.game.item.component;

import com.github.steveice10.mc.protocol.data.game.Holder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class BannerPatternLayer {
    private final Holder<BannerPattern> pattern;
    private final int colorId;

    @Data
    @AllArgsConstructor
    public static class BannerPattern {
        private final String assetId;
        private final String translationKey;
    }
}

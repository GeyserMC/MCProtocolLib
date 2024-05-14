package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.ResourceLocation;

@Data
@AllArgsConstructor
public class BannerPatternLayer {
    private final Holder<BannerPattern> pattern;
    private final int colorId;

    @Data
    @AllArgsConstructor
    public static class BannerPattern {
        private final ResourceLocation assetId;
        private final String translationKey;
    }
}

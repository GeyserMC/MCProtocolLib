package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;

@Data
@With
@AllArgsConstructor
@Builder(toBuilder = true)
public class BannerPatternLayer {
    private final Holder<BannerPattern> pattern;
    private final int colorId;

    @Data
    @With
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class BannerPattern {
        private final Key assetId;
        private final String translationKey;
    }
}

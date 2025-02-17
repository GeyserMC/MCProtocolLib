package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class PotionContents {
    private final int potionId;
    private final int customColor;
    private final List<MobEffectInstance> customEffects;
    private final @Nullable String customName;

    public PotionContents(int potionId, int customColor, List<MobEffectInstance> customEffects, @Nullable String customName) {
        this.potionId = potionId;
        this.customColor = customColor;
        this.customEffects = List.copyOf(customEffects);
        this.customName = customName;
    }
}

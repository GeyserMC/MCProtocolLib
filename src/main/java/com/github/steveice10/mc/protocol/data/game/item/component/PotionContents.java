package com.github.steveice10.mc.protocol.data.game.item.component;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class PotionContents {
    private final int potionId;
    private final int customColor;
    private final Int2ObjectMap<MobEffectDetails> customEffects;

    @Data
    @AllArgsConstructor
    public static class MobEffectDetails {
        private final int amplifier;
        private final int duration;
        private final boolean ambient;
        private final boolean showParticles;
        private final boolean showIcon;
        private final @Nullable MobEffectDetails hiddenEffect;
    }
}

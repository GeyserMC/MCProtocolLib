package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

import java.util.List;

@Data
@AllArgsConstructor
public class FoodProperties {
    private final int nutrition;
    private final float saturationModifier;
    private final boolean canAlwaysEat;
    private final float eatSeconds;
    private final @Nullable ItemStack usingConvertsTo;
    private final List<PossibleEffect> effects;

    @Data
    @AllArgsConstructor
    public static class PossibleEffect {
        private final MobEffectInstance effect;
        private final float probability;
    }
}

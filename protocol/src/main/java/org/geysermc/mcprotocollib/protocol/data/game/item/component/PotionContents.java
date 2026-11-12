package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

@Data
public class PotionContents {
    private final int potionId;
    private final OptionalInt customColor;
    private final List<MobEffectInstance> customEffects;
    private final @Nullable String customName;

    /** Retains the legacy convention that negative colors mean absent. */
    public PotionContents(int potionId, int customColor, List<MobEffectInstance> customEffects, @Nullable String customName) {
        this(potionId, customColor < 0 ? OptionalInt.empty() : OptionalInt.of(customColor), customEffects, customName);
    }

    @Builder(toBuilder = true)
    public PotionContents(int potionId, OptionalInt customColor, List<MobEffectInstance> customEffects, @Nullable String customName) {
        this.potionId = potionId;
        this.customColor = Objects.requireNonNull(customColor, "customColor");
        this.customEffects = List.copyOf(customEffects);
        this.customName = customName;
    }

    /** Use hasCustomColor() to distinguish an absent color from a present negative color. */
    public int getCustomColor() {
        return this.customColor.orElse(-1);
    }

    public boolean hasCustomColor() {
        return this.customColor.isPresent();
    }

    public static class PotionContentsBuilder {
        private OptionalInt customColor = OptionalInt.empty();

        public PotionContentsBuilder customColor(int customColor) {
            return customColor(customColor < 0 ? OptionalInt.empty() : OptionalInt.of(customColor));
        }

        public PotionContentsBuilder customColor(OptionalInt customColor) {
            this.customColor = Objects.requireNonNull(customColor, "customColor");
            return this;
        }
    }
}

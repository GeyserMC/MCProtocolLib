package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

import java.util.List;

@Builder(toBuilder = true)
public record Consumable(float consumeSeconds, ItemUseAnimation animation, Sound sound, boolean hasConsumeParticles, List<ConsumeEffect> onConsumeEffects) {
    public Consumable(float consumeSeconds, ItemUseAnimation animation, Sound sound, boolean hasConsumeParticles, List<ConsumeEffect> onConsumeEffects) {
        this.consumeSeconds = consumeSeconds;
        this.animation = animation;
        this.sound = sound;
        this.hasConsumeParticles = hasConsumeParticles;
        this.onConsumeEffects = List.copyOf(onConsumeEffects);
    }

    public enum ItemUseAnimation {
        NONE,
        EAT,
        DRINK,
        BLOCK,
        BOW,
        SPEAR,
        CROSSBOW,
        SPYGLASS,
        TOOT_HORN,
        BRUSH,
        BUNDLE;

        private static final ItemUseAnimation[] VALUES = values();

        public static ItemUseAnimation from(int id) {
            return VALUES[id];
        }
    }
}

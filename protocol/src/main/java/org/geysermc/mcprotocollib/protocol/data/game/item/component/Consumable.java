package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

import java.util.List;

public record Consumable(float consumeSeconds, ItemUseAnimation animation, Sound sound, boolean hasConsumeParticles, List<ConsumeEffect> onConsumeEffects) {

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
        BRUSH;

        private static final ItemUseAnimation[] VALUES = values();

        public static ItemUseAnimation from(int id) {
            return VALUES[id];
        }
    }
}

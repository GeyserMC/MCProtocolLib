package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

import java.util.List;

public interface ConsumeEffect {

    @Builder(toBuilder = true)
    record ApplyEffects(List<MobEffectInstance> effects, float probability) implements ConsumeEffect {
        public ApplyEffects(List<MobEffectInstance> effects, float probability) {
            this.effects = List.copyOf(effects);
            this.probability = probability;
        }
    }

    record RemoveEffects(HolderSet effects) implements ConsumeEffect {
    }

    record ClearAllEffects() implements ConsumeEffect {
    }

    record TeleportRandomly(float diameter) implements ConsumeEffect {
    }

    record PlaySound(Sound sound) implements ConsumeEffect {
    }
}

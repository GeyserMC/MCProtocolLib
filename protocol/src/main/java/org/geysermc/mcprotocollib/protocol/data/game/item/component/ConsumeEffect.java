package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

import java.util.List;

public interface ConsumeEffect {

    record ApplyEffects(List<MobEffectInstance> effects, float probability) implements ConsumeEffect {
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

package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class MobEffectDetails {
    private final @NonNull Effect effect;
    private final int amplifier;
    private final int duration;
    private final boolean ambient;
    private final boolean showParticles;
    private final boolean showIcon;
    private final @Nullable MobEffectDetails hiddenEffect;
}
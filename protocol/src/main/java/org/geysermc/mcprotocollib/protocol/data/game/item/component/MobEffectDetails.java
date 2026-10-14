package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class MobEffectDetails {
    private final int amplifier;
    private final int duration;
    private final boolean ambient;
    private final boolean showParticles;
    private final boolean showIcon;
    private final @Nullable MobEffectDetails hiddenEffect;
}

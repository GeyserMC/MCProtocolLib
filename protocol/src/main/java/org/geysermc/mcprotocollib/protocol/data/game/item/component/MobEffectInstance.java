package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.entity.Effect;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class MobEffectInstance {
    private final @NonNull Effect effect;
    private final @NonNull MobEffectDetails details;
}

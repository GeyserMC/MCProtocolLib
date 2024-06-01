package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PotionContents {
    private final int potionId;
    private final int customColor;
    private final List<MobEffectInstance> customEffects;
}

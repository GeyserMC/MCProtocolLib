package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PotionContents {
    private final int potionId;
    private final int customColor;
    private final Int2ObjectMap<MobEffectDetails> customEffects;
}

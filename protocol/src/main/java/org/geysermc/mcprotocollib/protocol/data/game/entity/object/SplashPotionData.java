package org.geysermc.mcprotocollib.protocol.data.game.entity.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SplashPotionData implements ObjectData {
    private final int potionData;
}

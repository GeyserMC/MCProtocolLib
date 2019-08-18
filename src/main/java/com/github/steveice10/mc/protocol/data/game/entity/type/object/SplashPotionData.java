package com.github.steveice10.mc.protocol.data.game.entity.type.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SplashPotionData implements ObjectData {
    private final int potionData;
}

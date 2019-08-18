package com.github.steveice10.mc.protocol.data.game.world.effect;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BreakPotionEffectData implements WorldEffectData {
    private final int potionId;
}

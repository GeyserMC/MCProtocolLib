package com.github.steveice10.mc.protocol.data.game.level.effect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class BreakBlockEffectData implements WorldEffectData {
    private final @NonNull int blockState;
}

package com.github.steveice10.mc.protocol.data.game.world.effect;

import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class BreakBlockEffectData implements WorldEffectData {
    private final @NonNull BlockState blockState;
}

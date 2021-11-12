package com.github.steveice10.mc.protocol.data.game.level.particle;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ItemParticleData implements ParticleData {
    private final @NonNull ItemStack itemStack;
}

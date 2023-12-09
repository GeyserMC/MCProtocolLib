package com.github.steveice10.mc.protocol.data.game.level.particle;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ItemParticleData(@Nullable ItemStack itemStack) implements ParticleData {
}

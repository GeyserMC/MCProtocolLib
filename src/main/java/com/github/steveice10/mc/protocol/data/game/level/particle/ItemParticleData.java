package com.github.steveice10.mc.protocol.data.game.level.particle;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemParticleData(@NotNull ItemStack itemStack) implements ParticleData {
}

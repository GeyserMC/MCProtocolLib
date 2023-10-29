package com.github.steveice10.mc.protocol.data.game.level.particle;

import org.jetbrains.annotations.NotNull;

public record Particle(@NotNull ParticleType type, ParticleData data) {
}

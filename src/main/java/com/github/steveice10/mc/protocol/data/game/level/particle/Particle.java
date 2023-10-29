package com.github.steveice10.mc.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Particle {
    private final @NotNull ParticleType type;
    private final ParticleData data;
}

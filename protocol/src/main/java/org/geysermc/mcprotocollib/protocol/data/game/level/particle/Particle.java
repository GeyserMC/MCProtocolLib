package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.Nullable;

@Data
@AllArgsConstructor
public class Particle {
    private final ParticleType type;
    private final @Nullable ParticleData data;
}

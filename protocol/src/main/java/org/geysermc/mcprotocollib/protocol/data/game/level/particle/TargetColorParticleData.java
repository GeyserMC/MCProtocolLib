package org.geysermc.mcprotocollib.protocol.data.game.level.particle;

import org.cloudburstmc.math.vector.Vector3d;

public record TargetColorParticleData(Vector3d target, int color) implements ParticleData {
}

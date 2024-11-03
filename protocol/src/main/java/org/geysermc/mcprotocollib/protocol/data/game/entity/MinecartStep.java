package org.geysermc.mcprotocollib.protocol.data.game.entity;

import org.cloudburstmc.math.vector.Vector3d;

public record MinecartStep(Vector3d position, Vector3d movement, float yRot, float xRot, float weight) {
}

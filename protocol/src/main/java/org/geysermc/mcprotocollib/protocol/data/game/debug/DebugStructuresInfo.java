package org.geysermc.mcprotocollib.protocol.data.game.debug;

import org.cloudburstmc.math.vector.Vector3i;

import java.util.List;

public record DebugStructuresInfo(List<StructureInfo> info) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.STRUCTURES;
    }

    public record StructureInfo(BoundingBox boundingBox, List<Piece> pieces) {
        public record Piece(BoundingBox boundingBox, boolean isStart) {}
    }

    public record BoundingBox(Vector3i min, Vector3i max) {}
}

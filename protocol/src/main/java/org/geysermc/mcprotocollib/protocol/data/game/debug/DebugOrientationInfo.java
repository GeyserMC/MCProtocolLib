package org.geysermc.mcprotocollib.protocol.data.game.debug;

public record DebugOrientationInfo(int orientationId) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS;
    }
}

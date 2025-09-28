package org.geysermc.mcprotocollib.protocol.data.game.debug;

public record DebugHiveInfo(int blockType, int occupantCount, int honeyLevel, boolean sedated) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.BEE_HIVES;
    }
}

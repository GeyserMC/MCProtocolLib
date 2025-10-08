package org.geysermc.mcprotocollib.protocol.data.game.debug;

public record DedicatedServerTickTimeInfo() implements DebugInfo {
    public static final DedicatedServerTickTimeInfo INSTANCE = new DedicatedServerTickTimeInfo();

    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.DEDICATED_SERVER_TICK_TIME;
    }
}

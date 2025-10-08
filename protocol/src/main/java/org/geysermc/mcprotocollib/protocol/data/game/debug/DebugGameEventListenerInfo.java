package org.geysermc.mcprotocollib.protocol.data.game.debug;

public record DebugGameEventListenerInfo(int listenerRadius) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.GAME_EVENT_LISTENERS;
    }
}

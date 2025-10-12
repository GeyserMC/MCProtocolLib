package org.geysermc.mcprotocollib.protocol.data.game.debug;

public record DebugVillageSectionsInfo() implements DebugInfo {
    public static final DebugVillageSectionsInfo INSTANCE = new DebugVillageSectionsInfo();

    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.VILLAGE_SECTIONS;
    }
}

package org.geysermc.mcprotocollib.protocol.data.game;

public enum ClientCommand {
    PERFORM_RESPAWN,
    REQUEST_STATS,
    REQUEST_GAMERULE_VALUES;

    private static final ClientCommand[] VALUES = values();

    public static ClientCommand from(int id) {
        return VALUES[id];
    }
}

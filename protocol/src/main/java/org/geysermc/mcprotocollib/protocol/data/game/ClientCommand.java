package org.geysermc.mcprotocollib.protocol.data.game;

public enum ClientCommand {
    RESPAWN,
    STATS;

    private static final ClientCommand[] VALUES = values();

    public static ClientCommand from(int id) {
        return VALUES[id];
    }
}

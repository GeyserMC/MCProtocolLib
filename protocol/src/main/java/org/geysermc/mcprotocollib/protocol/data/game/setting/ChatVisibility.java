package org.geysermc.mcprotocollib.protocol.data.game.setting;

public enum ChatVisibility {
    FULL,
    SYSTEM,
    HIDDEN;

    private static final ChatVisibility[] VALUES = values();

    public static ChatVisibility from(int id) {
        return VALUES[id];
    }
}

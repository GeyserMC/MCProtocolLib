package com.github.steveice10.mc.protocol.data.game;

public enum ResourcePackStatus {
    SUCCESSFULLY_LOADED,
    DECLINED,
    FAILED_DOWNLOAD,
    ACCEPTED,
    DOWNLOADED,
    INVALID_URL,
    FAILED_RELOAD,
    DISCARDED;

    private static final ResourcePackStatus[] VALUES = values();

    public static ResourcePackStatus from(int id) {
        return VALUES[id];
    }
}

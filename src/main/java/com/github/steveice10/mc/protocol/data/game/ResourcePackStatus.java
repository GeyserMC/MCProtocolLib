package com.github.steveice10.mc.protocol.data.game;

public enum ResourcePackStatus {
    SUCCESSFULLY_LOADED,
    DECLINED,
    FAILED_DOWNLOAD,
    ACCEPTED;

    private static final ResourcePackStatus[] VALUES = values();

    public static ResourcePackStatus from(int id) {
        return VALUES[id];
    }
}

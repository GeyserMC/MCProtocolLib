package com.github.steveice10.mc.protocol.data.game;

public enum RemoteDebugSampleType {
    TICK_TIME;

    private static final RemoteDebugSampleType[] VALUES = values();

    public static RemoteDebugSampleType from(int id) {
        return VALUES[id];
    }
}

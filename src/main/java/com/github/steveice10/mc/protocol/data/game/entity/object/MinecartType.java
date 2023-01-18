package com.github.steveice10.mc.protocol.data.game.entity.object;

public enum MinecartType implements ObjectData {
    NORMAL,
    CHEST,
    POWERED,
    TNT,
    MOB_SPAWNER,
    HOPPER,
    COMMAND_BLOCK;

    private static final MinecartType[] VALUES = values();

    public static MinecartType from(int id) {
        return VALUES[id];
    }
}

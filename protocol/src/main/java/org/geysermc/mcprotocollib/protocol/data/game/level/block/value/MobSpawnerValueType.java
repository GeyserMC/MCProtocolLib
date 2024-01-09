package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

public enum MobSpawnerValueType implements BlockValueType {
    RESET_DELAY;

    private static final MobSpawnerValueType[] VALUES = values();

    public static MobSpawnerValueType from(int id) {
        return VALUES[id];
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.entity.player;

public enum PlayerState {
    LEAVE_BED,
    START_SPRINTING,
    STOP_SPRINTING,
    START_HORSE_JUMP,
    STOP_HORSE_JUMP,
    OPEN_VEHICLE_INVENTORY,
    START_ELYTRA_FLYING;

    private static final PlayerState[] VALUES = values();

    public static PlayerState from(int id) {
        return VALUES[id];
    }
}

package org.geysermc.mcprotocollib.protocol.data.game.level.notify;

public enum GameEvent {
    INVALID_BED,
    START_RAIN,
    STOP_RAIN,
    CHANGE_GAMEMODE,
    ENTER_CREDITS,
    DEMO_MESSAGE,
    ARROW_HIT_PLAYER,
    RAIN_STRENGTH,
    THUNDER_STRENGTH,
    PUFFERFISH_STING_SOUND,
    AFFECTED_BY_ELDER_GUARDIAN,
    ENABLE_RESPAWN_SCREEN,
    LIMITED_CRAFTING,
    LEVEL_CHUNKS_LOAD_START;

    private static final GameEvent[] VALUES = values();

    public static GameEvent from(int id) {
        return VALUES[id];
    }
}

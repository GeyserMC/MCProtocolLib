package org.geysermc.mcprotocollib.protocol.data.game.level.notify;

public enum GameEvent {
    NO_RESPAWN_BLOCK_AVAILABLE,
    START_RAINING,
    STOP_RAINING,
    CHANGE_GAME_MODE,
    WIN_GAME,
    DEMO_EVENT,
    PLAY_ARROW_HIT_SOUND,
    RAIN_LEVEL_CHANGE,
    THUNDER_LEVEL_CHANGE,
    PUFFER_FISH_STING,
    GUARDIAN_ELDER_EFFECT,
    IMMEDIATE_RESPAWN,
    LIMITED_CRAFTING,
    LEVEL_CHUNKS_LOAD_START;

    private static final GameEvent[] VALUES = values();

    public static GameEvent from(int id) {
        return VALUES[id];
    }
}

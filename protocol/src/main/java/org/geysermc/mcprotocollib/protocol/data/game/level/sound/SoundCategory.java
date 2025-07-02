package org.geysermc.mcprotocollib.protocol.data.game.level.sound;

public enum SoundCategory {
    MASTER,
    MUSIC,
    RECORD,
    WEATHER,
    BLOCK,
    HOSTILE,
    NEUTRAL,
    PLAYER,
    AMBIENT,
    VOICE,
    UI;

    private static final SoundCategory[] VALUES = values();


    public static SoundCategory from(int id) {
        return VALUES[id];
    }
}

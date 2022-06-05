package com.github.steveice10.mc.protocol.data.game.level.sound;

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
    VOICE;

    private static final SoundCategory[] VALUES = values();


    public static SoundCategory from(int id) {
        return VALUES[id];
    }
}

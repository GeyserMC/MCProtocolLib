package com.github.steveice10.mc.protocol.data.game.level.sound;

import com.github.steveice10.packetlib.io.NetInput;

import java.io.IOException;

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

    public static SoundCategory read(NetInput in) throws IOException {
        return in.readEnum(VALUES);
    }

    public static SoundCategory fromId(int id) {
        return VALUES[id];
    }
}

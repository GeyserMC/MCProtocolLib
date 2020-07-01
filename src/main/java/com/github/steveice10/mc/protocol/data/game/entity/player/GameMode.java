package com.github.steveice10.mc.protocol.data.game.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.notify.ClientNotificationValue;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public enum GameMode implements ClientNotificationValue {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    public static GameMode readPreviousGameMode(int gamemode) {
        if (gamemode == 255) // https://bugs.mojang.com/browse/MC-189885
            return null; // Undefined gamemode; we're treating it as null
        return MagicValues.key(GameMode.class, gamemode);
    }

    public static void writePreviousGameMode(NetOutput out, GameMode gamemode) throws IOException {
        if (gamemode == null) {
            out.writeByte(255);
        } else {
            out.writeByte(MagicValues.value(Integer.class, gamemode));
        }
    }
}

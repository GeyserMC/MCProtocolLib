package com.github.steveice10.mc.protocol.data.game.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.notify.ClientNotificationValue;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public enum GameMode implements ClientNotificationValue {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR,
    UNKNOWN

}

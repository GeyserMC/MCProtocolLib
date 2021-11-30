package com.github.steveice10.mc.protocol.data.game.entity.player;

import com.github.steveice10.mc.protocol.data.game.level.notify.GameEventValue;

public enum GameMode implements GameEventValue {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR,
    UNKNOWN
}

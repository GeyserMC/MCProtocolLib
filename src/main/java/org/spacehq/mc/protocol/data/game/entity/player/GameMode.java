package org.spacehq.mc.protocol.data.game.entity.player;

import org.spacehq.mc.protocol.data.game.world.notify.ClientNotificationValue;

public enum GameMode implements ClientNotificationValue {

    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

}

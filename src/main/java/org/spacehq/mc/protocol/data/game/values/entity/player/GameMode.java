package org.spacehq.mc.protocol.data.game.values.entity.player;

import org.spacehq.mc.protocol.data.game.values.world.notify.ClientNotificationValue;

public enum GameMode implements ClientNotificationValue {

    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

}

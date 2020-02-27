package com.github.steveice10.mc.protocol.data.game.world.notify;

import lombok.Data;

@Data
public class RespawnScreenValue implements ClientNotificationValue {

    private final int respawnScreen;

    public RespawnScreenValue(int respawnScreen) {
        if(respawnScreen > 1) {
            respawnScreen = 1;
        }

        if(respawnScreen < 0) {
            respawnScreen = 0;
        }

        this.respawnScreen = respawnScreen;
    }
}

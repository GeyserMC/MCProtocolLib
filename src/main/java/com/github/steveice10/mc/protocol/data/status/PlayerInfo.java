package com.github.steveice10.mc.protocol.data.status;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

import java.util.Arrays;

public class PlayerInfo {
    private int max;
    private int online;
    private GameProfile players[];

    public PlayerInfo(int max, int online, GameProfile players[]) {
        this.max = max;
        this.online = online;
        this.players = players;
    }

    public int getMaxPlayers() {
        return this.max;
    }

    public int getOnlinePlayers() {
        return this.online;
    }

    public GameProfile[] getPlayers() {
        return this.players;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof PlayerInfo)) return false;

        PlayerInfo that = (PlayerInfo) o;
        return this.max == that.max &&
                this.online == that.online &&
                Arrays.equals(this.players, that.players);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.max, this.online, this.players);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

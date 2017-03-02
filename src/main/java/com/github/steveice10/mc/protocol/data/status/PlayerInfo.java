package com.github.steveice10.mc.protocol.data.status;

import com.github.steveice10.mc.auth.data.GameProfile;

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
        return o instanceof PlayerInfo && this.max == ((PlayerInfo) o).max && this.online == ((PlayerInfo) o).online && Arrays.deepEquals(this.players, ((PlayerInfo) o).players);
    }

    @Override
    public int hashCode() {
        int result = this.max;
        result = 31 * result + this.online;
        result = 31 * result + Arrays.deepHashCode(this.players);
        return result;
    }
}

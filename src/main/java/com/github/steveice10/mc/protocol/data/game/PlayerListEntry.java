package com.github.steveice10.mc.protocol.data.game;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.util.ReflectionToString;

public class PlayerListEntry {
    private GameProfile profile;

    private GameMode gameMode;
    private int ping;
    private Message displayName;

    public PlayerListEntry(GameProfile profile, GameMode gameMode, int ping, Message displayName) {
        this.profile = profile;
        this.gameMode = gameMode;
        this.ping = ping;
        this.displayName = displayName;
    }

    public PlayerListEntry(GameProfile profile, GameMode gameMode) {
        this.profile = profile;
        this.gameMode = gameMode;
    }

    public PlayerListEntry(GameProfile profile, int ping) {
        this.profile = profile;
        this.ping = ping;
    }

    public PlayerListEntry(GameProfile profile, Message displayName) {
        this.profile = profile;
        this.displayName = displayName;
    }

    public PlayerListEntry(GameProfile profile) {
        this.profile = profile;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public int getPing() {
        return this.ping;
    }

    public Message getDisplayName() {
        return this.displayName;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PlayerListEntry && this.profile.equals(((PlayerListEntry) o).profile) && this.gameMode == ((PlayerListEntry) o).gameMode && this.ping == ((PlayerListEntry) o).ping && (this.displayName != null ? this.displayName.equals(((PlayerListEntry) o).displayName) : ((PlayerListEntry) o).displayName == null);
    }

    @Override
    public int hashCode() {
        int result = this.profile.hashCode();
        result = 31 * result + (this.gameMode != null ? this.gameMode.hashCode() : 0);
        result = 31 * result + this.ping;
        result = 31 * result + (this.displayName != null ? this.displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}

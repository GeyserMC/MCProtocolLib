package org.spacehq.mc.protocol.data.game.values;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.message.Message;

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
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        PlayerListEntry entry = (PlayerListEntry) o;

        if(ping != entry.ping) return false;
        if(displayName != null ? !displayName.equals(entry.displayName) : entry.displayName != null) return false;
        if(gameMode != entry.gameMode) return false;
        if(!profile.equals(entry.profile)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = profile.hashCode();
        result = 31 * result + (gameMode != null ? gameMode.hashCode() : 0);
        result = 31 * result + ping;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

}

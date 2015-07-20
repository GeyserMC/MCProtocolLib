package org.spacehq.mc.protocol.data.status;

import org.spacehq.mc.protocol.data.message.Message;

import java.awt.image.BufferedImage;

public class ServerStatusInfo {

    private VersionInfo version;
    private PlayerInfo players;
    private Message description;
    private BufferedImage icon;

    public ServerStatusInfo(VersionInfo version, PlayerInfo players, Message description, BufferedImage icon) {
        this.version = version;
        this.players = players;
        this.description = description;
        this.icon = icon;
    }

    public VersionInfo getVersionInfo() {
        return this.version;
    }

    public PlayerInfo getPlayerInfo() {
        return this.players;
    }

    public Message getDescription() {
        return this.description;
    }

    public BufferedImage getIcon() {
        return this.icon;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        ServerStatusInfo that = (ServerStatusInfo) o;

        if(!description.equals(that.description)) return false;
        if(icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
        if(!players.equals(that.players)) return false;
        if(!version.equals(that.version)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = version.hashCode();
        result = 31 * result + players.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }

}

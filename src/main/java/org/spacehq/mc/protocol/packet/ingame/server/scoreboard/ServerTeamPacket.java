package org.spacehq.mc.protocol.packet.ingame.server.scoreboard;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.scoreboard.NameTagVisibility;
import org.spacehq.mc.protocol.data.game.values.scoreboard.TeamAction;
import org.spacehq.mc.protocol.data.game.values.scoreboard.TeamColor;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerTeamPacket implements Packet {

    private String name;
    private TeamAction action;
    private String displayName;
    private String prefix;
    private String suffix;
    private boolean friendlyFire;
    private boolean seeFriendlyInvisibles;
    private NameTagVisibility nameTagVisibility;
    private TeamColor color;
    private String players[];

    @SuppressWarnings("unused")
    private ServerTeamPacket() {
    }

    public ServerTeamPacket(String name) {
        this.name = name;
        this.action = TeamAction.REMOVE;
    }

    public ServerTeamPacket(String name, TeamAction action, String players[]) {
        if(action != TeamAction.ADD_PLAYER && action != TeamAction.REMOVE_PLAYER) {
            throw new IllegalArgumentException("(name, action, players) constructor only valid for adding and removing players.");
        }

        this.name = name;
        this.action = action;
        this.players = players;
    }

    public ServerTeamPacket(String name, String displayName, String prefix, String suffix, boolean friendlyFire, boolean seeFriendlyInvisibles, NameTagVisibility nameTagVisibility, TeamColor color) {
        this.name = name;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.friendlyFire = friendlyFire;
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.nameTagVisibility = nameTagVisibility;
        this.color = color;
        this.action = TeamAction.UPDATE;
    }

    public ServerTeamPacket(String name, String displayName, String prefix, String suffix, boolean friendlyFire, boolean seeFriendlyInvisibles, NameTagVisibility nameTagVisibility, TeamColor color, String players[]) {
        this.name = name;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.friendlyFire = friendlyFire;
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.nameTagVisibility = nameTagVisibility;
        this.color = color;
        this.players = players;
        this.action = TeamAction.CREATE;
    }

    public String getTeamName() {
        return this.name;
    }

    public TeamAction getAction() {
        return this.action;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public boolean getFriendlyFire() {
        return this.friendlyFire;
    }

    public boolean getSeeFriendlyInvisibles() {
        return seeFriendlyInvisibles;
    }

    public NameTagVisibility getNameTagVisibility() {
        return this.nameTagVisibility;
    }

    public TeamColor getColor() {
        return this.color;
    }

    public String[] getPlayers() {
        return this.players;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.name = in.readString();
        this.action = MagicValues.key(TeamAction.class, in.readByte());
        if(this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            this.displayName = in.readString();
            this.prefix = in.readString();
            this.suffix = in.readString();
            byte flags = in.readByte();
            this.friendlyFire = (flags & 0x1) != 0;
            this.seeFriendlyInvisibles = (flags & 0x2) != 0;
            this.nameTagVisibility = MagicValues.key(NameTagVisibility.class, in.readString());
            this.color = MagicValues.key(TeamColor.class, in.readByte());
        }

        if(this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
            this.players = new String[in.readVarInt()];
            for(int index = 0; index < this.players.length; index++) {
                this.players[index] = in.readString();
            }
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.name);
        out.writeByte(MagicValues.value(Integer.class, this.action));
        if(this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            out.writeString(this.displayName);
            out.writeString(this.prefix);
            out.writeString(this.suffix);
            byte flags = 0;
            if(this.friendlyFire) {
                flags |= 0x1;
            }
            if(this.seeFriendlyInvisibles) {
                flags |= 0x2;
            }
            out.writeByte(flags);
            out.writeString(MagicValues.value(String.class, this.nameTagVisibility));
            out.writeByte(MagicValues.value(Integer.class, this.color));
        }

        if(this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
            out.writeVarInt(this.players.length);
            for(String player : this.players) {
                if(player != null) {
                    out.writeString(player);
                }
            }
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}

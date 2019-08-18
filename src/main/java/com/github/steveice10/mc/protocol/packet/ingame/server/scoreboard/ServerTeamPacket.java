package com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.scoreboard.CollisionRule;
import com.github.steveice10.mc.protocol.data.game.scoreboard.NameTagVisibility;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamColor;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerTeamPacket implements Packet {
    private @NonNull String teamName;
    private @NonNull TeamAction action;

    private Message displayName;
    private Message prefix;
    private Message suffix;
    private boolean friendlyFire;
    private boolean seeFriendlyInvisibles;
    private NameTagVisibility nameTagVisibility;
    private CollisionRule collisionRule;
    private TeamColor color;

    private String[] players;

    public ServerTeamPacket(@NonNull String teamName) {
        this.teamName = teamName;
        this.action = TeamAction.REMOVE;
    }

    public ServerTeamPacket(@NonNull String teamName, @NonNull Message displayName, @NonNull Message prefix, @NonNull Message suffix,
                            boolean friendlyFire, boolean seeFriendlyInvisibles, @NonNull NameTagVisibility nameTagVisibility,
                            @NonNull CollisionRule collisionRule, @NonNull TeamColor color) {
        this.teamName = teamName;
        this.action = TeamAction.UPDATE;

        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.friendlyFire = friendlyFire;
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.nameTagVisibility = nameTagVisibility;
        this.collisionRule = collisionRule;
        this.color = color;
    }

    public ServerTeamPacket(@NonNull String teamName, @NonNull TeamAction action, @NonNull String[] players) {
        if(action != TeamAction.ADD_PLAYER && action != TeamAction.REMOVE_PLAYER) {
            throw new IllegalArgumentException("(name, action, players) constructor only valid for adding and removing players.");
        }

        this.teamName = teamName;
        this.action = action;

        this.players = Arrays.copyOf(players, players.length);
    }

    public ServerTeamPacket(@NonNull String teamName, @NonNull Message displayName, @NonNull Message prefix, @NonNull Message suffix,
                            boolean friendlyFire, boolean seeFriendlyInvisibles, @NonNull NameTagVisibility nameTagVisibility,
                            @NonNull CollisionRule collisionRule, @NonNull TeamColor color, @NonNull String[] players) {
        this.teamName = teamName;
        this.action = TeamAction.CREATE;

        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.friendlyFire = friendlyFire;
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.nameTagVisibility = nameTagVisibility;
        this.collisionRule = collisionRule;
        this.color = color;

        this.players = Arrays.copyOf(players, players.length);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.teamName = in.readString();
        this.action = MagicValues.key(TeamAction.class, in.readByte());
        if(this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            this.displayName = Message.fromString(in.readString());
            byte flags = in.readByte();
            this.friendlyFire = (flags & 0x1) != 0;
            this.seeFriendlyInvisibles = (flags & 0x2) != 0;
            this.nameTagVisibility = MagicValues.key(NameTagVisibility.class, in.readString());
            this.collisionRule = MagicValues.key(CollisionRule.class, in.readString());

            try {
                this.color = MagicValues.key(TeamColor.class, in.readVarInt());
            } catch(IllegalArgumentException e) {
                this.color = TeamColor.NONE;
            }

            this.prefix = Message.fromString(in.readString());
            this.suffix = Message.fromString(in.readString());
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
        out.writeString(this.teamName);
        out.writeByte(MagicValues.value(Integer.class, this.action));
        if(this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            out.writeString(this.displayName.toJsonString());
            out.writeByte((this.friendlyFire ? 0x1 : 0x0) | (this.seeFriendlyInvisibles ? 0x2 : 0x0));
            out.writeString(MagicValues.value(String.class, this.nameTagVisibility));
            out.writeString(MagicValues.value(String.class, this.collisionRule));
            out.writeVarInt(MagicValues.value(Integer.class, this.color));
            out.writeString(this.prefix.toJsonString());
            out.writeString(this.suffix.toJsonString());
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

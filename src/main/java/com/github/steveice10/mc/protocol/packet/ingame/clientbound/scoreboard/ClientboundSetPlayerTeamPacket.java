package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.scoreboard.CollisionRule;
import com.github.steveice10.mc.protocol.data.game.scoreboard.NameTagVisibility;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamColor;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.Arrays;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundSetPlayerTeamPacket implements Packet {
    private final @NonNull String teamName;
    private final @NonNull TeamAction action;

    private final Component displayName;
    private final Component prefix;
    private final Component suffix;
    private final boolean friendlyFire;
    private final boolean seeFriendlyInvisibles;
    private final NameTagVisibility nameTagVisibility;
    private final CollisionRule collisionRule;
    private final TeamColor color;

    private final String[] players;

    public ClientboundSetPlayerTeamPacket(@NonNull String teamName) {
        this.teamName = teamName;
        this.action = TeamAction.REMOVE;

        this.displayName = null;
        this.prefix = null;
        this.suffix = null;
        this.friendlyFire = false;
        this.seeFriendlyInvisibles = false;
        this.nameTagVisibility = null;
        this.collisionRule = null;
        this.color = null;

        this.players = null;
    }

    public ClientboundSetPlayerTeamPacket(@NonNull String teamName, @NonNull Component displayName, @NonNull Component prefix, @NonNull Component suffix,
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

        this.players = null;
    }

    public ClientboundSetPlayerTeamPacket(@NonNull String teamName, @NonNull TeamAction action, @NonNull String[] players) {
        if (action != TeamAction.ADD_PLAYER && action != TeamAction.REMOVE_PLAYER) {
            throw new IllegalArgumentException("(name, action, players) constructor only valid for adding and removing players.");
        }

        this.teamName = teamName;
        this.action = action;

        this.displayName = null;
        this.prefix = null;
        this.suffix = null;
        this.friendlyFire = false;
        this.seeFriendlyInvisibles = false;
        this.nameTagVisibility = null;
        this.collisionRule = null;
        this.color = null;

        this.players = Arrays.copyOf(players, players.length);
    }

    public ClientboundSetPlayerTeamPacket(@NonNull String teamName, @NonNull Component displayName, @NonNull Component prefix, @NonNull Component suffix,
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

    public ClientboundSetPlayerTeamPacket(NetInput in) throws IOException {
        this.teamName = in.readString();
        this.action = MagicValues.key(TeamAction.class, in.readByte());
        if (this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            this.displayName = DefaultComponentSerializer.get().deserialize(in.readString());
            byte flags = in.readByte();
            this.friendlyFire = (flags & 0x1) != 0;
            this.seeFriendlyInvisibles = (flags & 0x2) != 0;
            this.nameTagVisibility = MagicValues.key(NameTagVisibility.class, in.readString());
            this.collisionRule = MagicValues.key(CollisionRule.class, in.readString());

            this.color = TeamColor.VALUES[in.readVarInt()];

            this.prefix = DefaultComponentSerializer.get().deserialize(in.readString());
            this.suffix = DefaultComponentSerializer.get().deserialize(in.readString());
        } else {
            this.displayName = null;
            this.prefix = null;
            this.suffix = null;
            this.friendlyFire = false;
            this.seeFriendlyInvisibles = false;
            this.nameTagVisibility = null;
            this.collisionRule = null;
            this.color = null;
        }

        if (this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
            this.players = new String[in.readVarInt()];
            for (int index = 0; index < this.players.length; index++) {
                this.players[index] = in.readString();
            }
        } else {
            this.players = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.teamName);
        out.writeByte(MagicValues.value(Integer.class, this.action));
        if (this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            out.writeString(DefaultComponentSerializer.get().serialize(this.displayName));
            out.writeByte((this.friendlyFire ? 0x1 : 0x0) | (this.seeFriendlyInvisibles ? 0x2 : 0x0));
            out.writeString(MagicValues.value(String.class, this.nameTagVisibility));
            out.writeString(MagicValues.value(String.class, this.collisionRule));
            out.writeVarInt(this.color.ordinal());
            out.writeString(DefaultComponentSerializer.get().serialize(this.prefix));
            out.writeString(DefaultComponentSerializer.get().serialize(this.suffix));
        }

        if (this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
            out.writeVarInt(this.players.length);
            for (String player : this.players) {
                if (player != null) {
                    out.writeString(player);
                }
            }
        }
    }
}

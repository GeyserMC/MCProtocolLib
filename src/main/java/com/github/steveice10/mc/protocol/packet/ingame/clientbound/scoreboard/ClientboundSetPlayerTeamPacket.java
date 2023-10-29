package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.scoreboard.CollisionRule;
import com.github.steveice10.mc.protocol.data.game.scoreboard.NameTagVisibility;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamColor;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundSetPlayerTeamPacket implements MinecraftPacket {
    private final @NotNull String teamName;
    private final @NotNull TeamAction action;

    private final Component displayName;
    private final Component prefix;
    private final Component suffix;
    private final boolean friendlyFire;
    private final boolean seeFriendlyInvisibles;
    private final @Nullable NameTagVisibility nameTagVisibility;
    private final @Nullable CollisionRule collisionRule;
    private final TeamColor color;

    private final String[] players;

    public ClientboundSetPlayerTeamPacket(@NotNull String teamName) {
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

    public ClientboundSetPlayerTeamPacket(@NotNull String teamName, @NotNull Component displayName, @NotNull Component prefix, @NotNull Component suffix,
                                          boolean friendlyFire, boolean seeFriendlyInvisibles, @NotNull NameTagVisibility nameTagVisibility,
                                          @NotNull CollisionRule collisionRule, @NotNull TeamColor color) {
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

    public ClientboundSetPlayerTeamPacket(@NotNull String teamName, @NotNull TeamAction action, @NotNull String[] players) {
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

    public ClientboundSetPlayerTeamPacket(@NotNull String teamName, @NotNull Component displayName, @NotNull Component prefix, @NotNull Component suffix,
                                          boolean friendlyFire, boolean seeFriendlyInvisibles, @NotNull NameTagVisibility nameTagVisibility,
                                          @NotNull CollisionRule collisionRule, @NotNull TeamColor color, @NotNull String[] players) {
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

    public ClientboundSetPlayerTeamPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.teamName = helper.readString(in);
        this.action = TeamAction.from(in.readByte());
        if (this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            this.displayName = helper.readComponent(in);
            byte flags = in.readByte();
            this.friendlyFire = (flags & 0x1) != 0;
            this.seeFriendlyInvisibles = (flags & 0x2) != 0;
            this.nameTagVisibility = NameTagVisibility.from(helper.readString(in));
            this.collisionRule = CollisionRule.from(helper.readString(in));

            this.color = TeamColor.VALUES[helper.readVarInt(in)];

            this.prefix = helper.readComponent(in);
            this.suffix = helper.readComponent(in);
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
            this.players = new String[helper.readVarInt(in)];
            for (int index = 0; index < this.players.length; index++) {
                this.players[index] = helper.readString(in);
            }
        } else {
            this.players = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.teamName);
        out.writeByte(this.action.ordinal());
        if (this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            helper.writeComponent(out, this.displayName);
            out.writeByte((this.friendlyFire ? 0x1 : 0x0) | (this.seeFriendlyInvisibles ? 0x2 : 0x0));
            helper.writeString(out, this.nameTagVisibility == null ? "" : this.nameTagVisibility.getName());
            helper.writeString(out, this.collisionRule == null ? "" : this.collisionRule.getName());
            helper.writeVarInt(out, this.color.ordinal());
            helper.writeComponent(out, this.prefix);
            helper.writeComponent(out, this.suffix);
        }

        if (this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
            helper.writeVarInt(out, this.players.length);
            for (String player : this.players) {
                if (player != null) {
                    helper.writeString(out, player);
                }
            }
        }
    }
}

package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.CollisionRule;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.NameTagVisibility;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.TeamAction;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.TeamColor;

import java.util.Arrays;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundSetPlayerTeamPacket implements MinecraftPacket {
    private final @NonNull String teamName;
    private final @NonNull TeamAction action;

    private final Component displayName;
    private final Component playerPrefix;
    private final Component playerSuffix;
    private final @Nullable NameTagVisibility nameTagVisibility;
    private final @Nullable CollisionRule collisionRule;
    private final @Nullable TeamColor color;
    private final boolean friendlyFire;
    private final boolean seeFriendlyInvisibles;

    private final String[] players;

    public ClientboundSetPlayerTeamPacket(@NonNull String teamName) {
        this.teamName = teamName;
        this.action = TeamAction.REMOVE;

        this.displayName = null;
        this.playerPrefix = null;
        this.playerSuffix = null;
        this.friendlyFire = false;
        this.seeFriendlyInvisibles = false;
        this.nameTagVisibility = null;
        this.collisionRule = null;
        this.color = null;

        this.players = null;
    }

    public ClientboundSetPlayerTeamPacket(@NonNull String teamName, @NonNull Component displayName, @NonNull Component playerPrefix, @NonNull Component playerSuffix,
                                          boolean friendlyFire, boolean seeFriendlyInvisibles, @NonNull NameTagVisibility nameTagVisibility,
                                          @NonNull CollisionRule collisionRule, @Nullable TeamColor color) {
        this.teamName = teamName;
        this.action = TeamAction.UPDATE;

        this.displayName = displayName;
        this.playerPrefix = playerPrefix;
        this.playerSuffix = playerSuffix;
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
        this.playerPrefix = null;
        this.playerSuffix = null;
        this.friendlyFire = false;
        this.seeFriendlyInvisibles = false;
        this.nameTagVisibility = null;
        this.collisionRule = null;
        this.color = null;

        this.players = Arrays.copyOf(players, players.length);
    }

    public ClientboundSetPlayerTeamPacket(@NonNull String teamName, @NonNull Component displayName, @NonNull Component playerPrefix, @NonNull Component playerSuffix,
                                          boolean friendlyFire, boolean seeFriendlyInvisibles, @NonNull NameTagVisibility nameTagVisibility,
                                          @NonNull CollisionRule collisionRule, @Nullable TeamColor color, @NonNull String[] players) {
        this.teamName = teamName;
        this.action = TeamAction.CREATE;

        this.displayName = displayName;
        this.playerPrefix = playerPrefix;
        this.playerSuffix = playerSuffix;
        this.friendlyFire = friendlyFire;
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.nameTagVisibility = nameTagVisibility;
        this.collisionRule = collisionRule;
        this.color = color;

        this.players = Arrays.copyOf(players, players.length);
    }

    public ClientboundSetPlayerTeamPacket(ByteBuf in) {
        this.teamName = MinecraftTypes.readString(in);
        this.action = TeamAction.from(in.readByte());
        if (this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            this.displayName = MinecraftTypes.readComponent(in);
            this.playerPrefix = MinecraftTypes.readComponent(in);
            this.playerSuffix = MinecraftTypes.readComponent(in);
            this.nameTagVisibility = NameTagVisibility.from(MinecraftTypes.readVarInt(in));
            this.collisionRule = CollisionRule.from(MinecraftTypes.readVarInt(in));

            if (in.readBoolean()) {
                this.color = TeamColor.from(MinecraftTypes.readVarInt(in));
            } else {
                this.color = null;
            }

            byte flags = in.readByte();
            this.friendlyFire = (flags & 0x1) != 0;
            this.seeFriendlyInvisibles = (flags & 0x2) != 0;
        } else {
            this.displayName = null;
            this.playerPrefix = null;
            this.playerSuffix = null;
            this.friendlyFire = false;
            this.seeFriendlyInvisibles = false;
            this.nameTagVisibility = null;
            this.collisionRule = null;
            this.color = null;
        }

        if (this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
            this.players = new String[MinecraftTypes.readVarInt(in)];
            for (int index = 0; index < this.players.length; index++) {
                this.players[index] = MinecraftTypes.readString(in);
            }
        } else {
            this.players = null;
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.teamName);
        out.writeByte(this.action.ordinal());
        if (this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            MinecraftTypes.writeComponent(out, this.displayName);
            MinecraftTypes.writeComponent(out, this.playerPrefix);
            MinecraftTypes.writeComponent(out, this.playerSuffix);
            MinecraftTypes.writeVarInt(out, this.nameTagVisibility.ordinal());
            MinecraftTypes.writeVarInt(out, this.collisionRule.ordinal());
            if (this.color != null) {
                out.writeBoolean(true);
                MinecraftTypes.writeVarInt(out, this.color.ordinal());
            } else {
                out.writeBoolean(false);
            }

            out.writeByte((this.friendlyFire ? 0x1 : 0x0) | (this.seeFriendlyInvisibles ? 0x2 : 0x0));
        }

        if (this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
            MinecraftTypes.writeVarInt(out, this.players.length);
            for (String player : this.players) {
                if (player != null) {
                    MinecraftTypes.writeString(out, player);
                }
            }
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}

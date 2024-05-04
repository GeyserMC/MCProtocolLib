package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
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
    private final Component prefix;
    private final Component suffix;
    private final boolean friendlyFire;
    private final boolean seeFriendlyInvisibles;
    private final @Nullable NameTagVisibility nameTagVisibility;
    private final @Nullable CollisionRule collisionRule;
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

    public ClientboundSetPlayerTeamPacket(MinecraftByteBuf buf) {
        this.teamName = buf.readString();
        this.action = TeamAction.from(buf.readByte());
        if (this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            this.displayName = buf.readComponent();
            byte flags = buf.readByte();
            this.friendlyFire = (flags & 0x1) != 0;
            this.seeFriendlyInvisibles = (flags & 0x2) != 0;
            this.nameTagVisibility = NameTagVisibility.from(buf.readString());
            this.collisionRule = CollisionRule.from(buf.readString());

            this.color = TeamColor.VALUES[buf.readVarInt()];

            this.prefix = buf.readComponent();
            this.suffix = buf.readComponent();
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
            this.players = new String[buf.readVarInt()];
            for (int index = 0; index < this.players.length; index++) {
                this.players[index] = buf.readString();
            }
        } else {
            this.players = null;
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.teamName);
        buf.writeByte(this.action.ordinal());
        if (this.action == TeamAction.CREATE || this.action == TeamAction.UPDATE) {
            buf.writeComponent(this.displayName);
            buf.writeByte((this.friendlyFire ? 0x1 : 0x0) | (this.seeFriendlyInvisibles ? 0x2 : 0x0));
            buf.writeString(this.nameTagVisibility == null ? "" : this.nameTagVisibility.getName());
            buf.writeString(this.collisionRule == null ? "" : this.collisionRule.getName());
            buf.writeVarInt(this.color.ordinal());
            buf.writeComponent(this.prefix);
            buf.writeComponent(this.suffix);
        }

        if (this.action == TeamAction.CREATE || this.action == TeamAction.ADD_PLAYER || this.action == TeamAction.REMOVE_PLAYER) {
            buf.writeVarInt(this.players.length);
            for (String player : this.players) {
                if (player != null) {
                    buf.writeString(player);
                }
            }
        }
    }
}

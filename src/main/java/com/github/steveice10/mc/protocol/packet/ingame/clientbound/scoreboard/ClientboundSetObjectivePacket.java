package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ObjectiveAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreType;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
public class ClientboundSetObjectivePacket implements MinecraftPacket {
    private final @NonNull String name;
    private final @NonNull ObjectiveAction action;

    private final Component displayName;
    private final ScoreType type;

    /**
     * Constructs a ServerScoreboardObjectivePacket for removing an objective.
     *
     * @param name Name of the objective.
     */
    public ClientboundSetObjectivePacket(@NonNull String name) {
        this(name, ObjectiveAction.REMOVE, null, null);
    }

    /**
     * Constructs a ServerScoreboardObjectivePacket for adding or updating an objective.
     *
     * @param name        Name of the objective.
     * @param action      Action to perform.
     * @param displayName Display name of the objective.
     * @param type        Type of score.
     */
    public ClientboundSetObjectivePacket(@NonNull String name, @NonNull ObjectiveAction action, Component displayName, ScoreType type) {
        if ((action == ObjectiveAction.ADD || action == ObjectiveAction.UPDATE) && (displayName == null || type == null)) {
            throw new IllegalArgumentException("ADD and UPDATE actions require display name and type.");
        }

        this.name = name;
        this.action = action;
        this.displayName = displayName;
        this.type = type;
    }

    public ClientboundSetObjectivePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.name = helper.readString(in);
        this.action = ObjectiveAction.from(in.readByte());
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            this.displayName = helper.readComponent(in);
            this.type = ScoreType.from(helper.readVarInt(in));
        } else {
            this.displayName = null;
            this.type = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.name);
        out.writeByte(this.action.ordinal());
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            helper.writeComponent(out, this.displayName);
            helper.writeVarInt(out, this.type.ordinal());
        }
    }
}

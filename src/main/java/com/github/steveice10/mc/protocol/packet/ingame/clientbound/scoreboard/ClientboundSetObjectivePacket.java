package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ObjectiveAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
public class ClientboundSetObjectivePacket implements Packet {
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

    public ClientboundSetObjectivePacket(NetInput in) throws IOException {
        this.name = in.readString();
        this.action = MagicValues.key(ObjectiveAction.class, in.readByte());
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            this.displayName = DefaultComponentSerializer.get().deserialize(in.readString());
            this.type = MagicValues.key(ScoreType.class, in.readVarInt());
        } else {
            this.displayName = null;
            this.type = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.name);
        out.writeByte(MagicValues.value(Integer.class, this.action));
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            out.writeString(DefaultComponentSerializer.get().serialize(this.displayName));
            out.writeVarInt(MagicValues.value(Integer.class, this.type));
        }
    }
}

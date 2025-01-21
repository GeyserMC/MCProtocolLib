package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.scoreboard;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.chat.numbers.NumberFormat;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.ObjectiveAction;
import org.geysermc.mcprotocollib.protocol.data.game.scoreboard.ScoreType;

@Data
@With
public class ClientboundSetObjectivePacket implements MinecraftPacket {
    private final @NonNull String name;
    private final @NonNull ObjectiveAction action;

    /**
     * Not null if {@link #getAction()} is {@link ObjectiveAction#ADD} or {@link ObjectiveAction#UPDATE}
     */
    private final @Nullable Component displayName;

    /**
     * Not null if {@link #getAction()} is {@link ObjectiveAction#ADD} or {@link ObjectiveAction#UPDATE}
     */
    private final @Nullable ScoreType type;
    private final @Nullable NumberFormat numberFormat;

    /**
     * Constructs a ServerScoreboardObjectivePacket for removing an objective.
     *
     * @param name Name of the objective.
     */
    public ClientboundSetObjectivePacket(@NonNull String name) {
        this(name, ObjectiveAction.REMOVE, null, null, null);
    }

    /**
     * Constructs a ServerScoreboardObjectivePacket for adding or updating an objective.
     *
     * @param name Name of the objective.
     * @param action Action to perform.
     * @param displayName Display name of the objective.
     * @param type Type of score.
     * @param numberFormat Number formatting.
     */
    public ClientboundSetObjectivePacket(@NonNull String name, @NonNull ObjectiveAction action, @Nullable Component displayName, @Nullable ScoreType type, @Nullable NumberFormat numberFormat) {
        if ((action == ObjectiveAction.ADD || action == ObjectiveAction.UPDATE) && (displayName == null || type == null)) {
            throw new IllegalArgumentException("ADD and UPDATE actions require display name and type.");
        }

        this.name = name;
        this.action = action;
        this.displayName = displayName;
        this.type = type;
        this.numberFormat = numberFormat;
    }

    public ClientboundSetObjectivePacket(ByteBuf in) {
        this.name = MinecraftTypes.readString(in);
        this.action = ObjectiveAction.from(in.readByte());
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            this.displayName = MinecraftTypes.readComponent(in);
            this.type = ScoreType.from(MinecraftTypes.readVarInt(in));
            this.numberFormat = MinecraftTypes.readNullable(in, MinecraftTypes::readNumberFormat);
        } else {
            this.displayName = null;
            this.type = ScoreType.INTEGER;
            this.numberFormat = null;
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.name);
        out.writeByte(this.action.ordinal());
        if (this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            MinecraftTypes.writeComponent(out, this.displayName);
            MinecraftTypes.writeVarInt(out, this.type.ordinal());
            MinecraftTypes.writeNullable(out, this.numberFormat, MinecraftTypes::writeNumberFormat);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}

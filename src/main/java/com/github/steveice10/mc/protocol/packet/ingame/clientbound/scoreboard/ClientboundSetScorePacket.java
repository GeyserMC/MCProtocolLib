package com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardAction;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;

import java.io.IOException;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundSetScorePacket implements Packet {
    private final @NonNull String entry;
    private final @NonNull ScoreboardAction action;
    private final @NonNull String objective;
    private final int value;

    /**
     * Creates a set score packet that removes the entry.
     */
    public ClientboundSetScorePacket(@NonNull String entry, @NonNull String objective) {
        this.entry = entry;
        this.action = ScoreboardAction.REMOVE;
        this.objective = objective;
        this.value = 0;
    }

    /**
     * Creates a set score packet that adds or updates the entry.
     */
    public ClientboundSetScorePacket(@NonNull String entry, @NonNull String objective, int value) {
        this.entry = entry;
        this.action = ScoreboardAction.ADD_OR_UPDATE;
        this.objective = objective;

        this.value = value;
    }

    public ClientboundSetScorePacket(NetInput in) throws IOException {
        this.entry = in.readString();
        this.action = MagicValues.key(ScoreboardAction.class, in.readVarInt());
        this.objective = in.readString();
        if (this.action == ScoreboardAction.ADD_OR_UPDATE) {
            this.value = in.readVarInt();
        } else {
            this.value = 0;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.entry);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        out.writeString(this.objective);
        if (this.action == ScoreboardAction.ADD_OR_UPDATE) {
            out.writeVarInt(this.value);
        }
    }
}

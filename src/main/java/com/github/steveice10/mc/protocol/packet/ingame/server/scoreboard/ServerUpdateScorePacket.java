package com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardAction;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerUpdateScorePacket implements Packet {
    private @NonNull String entry;
    private @NonNull ScoreboardAction action;
    private @NonNull String objective;
    private int value;

    public ServerUpdateScorePacket(@NonNull String entry, @NonNull String objective) {
        this.entry = entry;
        this.action = ScoreboardAction.REMOVE;
        this.objective = objective;
    }

    public ServerUpdateScorePacket(@NonNull String entry, @NonNull String objective, int value) {
        this.entry = entry;
        this.action = ScoreboardAction.ADD_OR_UPDATE;
        this.objective = objective;

        this.value = value;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entry = in.readString();
        this.action = MagicValues.key(ScoreboardAction.class, in.readVarInt());
        this.objective = in.readString();
        if (this.action == ScoreboardAction.ADD_OR_UPDATE) {
            this.value = in.readVarInt();
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

    @Override
    public boolean isPriority() {
        return false;
    }
}

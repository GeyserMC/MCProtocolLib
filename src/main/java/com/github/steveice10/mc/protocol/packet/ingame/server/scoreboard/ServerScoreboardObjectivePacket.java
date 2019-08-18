package com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ObjectiveAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreType;
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

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerScoreboardObjectivePacket implements Packet {
    private @NonNull String name;
    private @NonNull ObjectiveAction action;

    private Message displayName;
    private ScoreType type;

    public ServerScoreboardObjectivePacket(@NonNull String name) {
        this.name = name;
        this.action = ObjectiveAction.REMOVE;
    }

    public ServerScoreboardObjectivePacket(@NonNull String name, @NonNull ObjectiveAction action, @NonNull Message displayName, @NonNull ScoreType type) {
        if(action != ObjectiveAction.ADD && action != ObjectiveAction.UPDATE) {
            throw new IllegalArgumentException("(name, action, displayName, type) constructor only valid for adding and updating objectives.");
        }

        this.name = name;
        this.action = action;
        this.displayName = displayName;
        this.type = type;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.name = in.readString();
        this.action = MagicValues.key(ObjectiveAction.class, in.readByte());
        if(this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            this.displayName = Message.fromString(in.readString());
            this.type = MagicValues.key(ScoreType.class, in.readVarInt());
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.name);
        out.writeByte(MagicValues.value(Integer.class, this.action));
        if(this.action == ObjectiveAction.ADD || this.action == ObjectiveAction.UPDATE) {
            out.writeString(this.displayName.toJsonString());
            out.writeVarInt(MagicValues.value(Integer.class, this.type));
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

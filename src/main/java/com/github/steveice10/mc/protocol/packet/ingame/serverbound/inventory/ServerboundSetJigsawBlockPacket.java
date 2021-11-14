package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundSetJigsawBlockPacket implements Packet {
    private final @NonNull Position position;
    private final @NonNull String name;
    private final @NonNull String target;
    private final @NonNull String pool;
    private final @NonNull String finalState;
    private final @NonNull String jointType;

    public ServerboundSetJigsawBlockPacket(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.name = in.readString();
        this.target = in.readString();
        this.pool = in.readString();
        this.finalState = in.readString();
        this.jointType = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        out.writeString(this.name);
        out.writeString(this.target);
        out.writeString(this.pool);
        out.writeString(this.finalState);
        out.writeString(this.jointType);
    }
}

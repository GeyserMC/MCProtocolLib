package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientUpdateJigsawBlockPacket implements Packet {
    private @NonNull Position position;
    private @NonNull String attachmentType;
    private @NonNull String targetPool;
    private @NonNull String finalState;

    @Override
    public void read(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.attachmentType = in.readString();
        this.targetPool = in.readString();
        this.finalState = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        out.writeString(this.attachmentType);
        out.writeString(this.targetPool);
        out.writeString(this.finalState);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

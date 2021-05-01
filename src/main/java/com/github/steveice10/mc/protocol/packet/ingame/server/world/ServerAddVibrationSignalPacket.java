package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;

import java.io.IOException;

//TODO
@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerAddVibrationSignalPacket implements Packet {
    private Position origin;
    private String identifier;
    private int arrivalInTicks;

    @Override
    public void read(NetInput in) throws IOException {
        this.origin = Position.read(in);
        this.identifier = in.readString();
        // more to do
        this.arrivalInTicks = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.origin);
        out.writeString(this.identifier);
        // ...
        out.writeVarInt(this.arrivalInTicks);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

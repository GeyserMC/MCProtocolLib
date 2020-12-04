package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

//TODO
@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerSculkSensorPacket implements Packet {
    private Position position;
    private String identifier;

    @Override
    public void read(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.identifier = in.readString();
        // more to do
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        out.writeString(this.identifier);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

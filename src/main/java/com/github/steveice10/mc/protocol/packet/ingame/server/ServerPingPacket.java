package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerPingPacket implements Packet {
    private int id;

    @Override
    public void read(NetInput in) throws IOException {
        this.id = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.id);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

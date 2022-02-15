package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTimePacket implements Packet {
    private final long worldAge;
    private final long time;

    public ClientboundSetTimePacket(NetInput in) throws IOException {
        this.worldAge = in.readLong();
        this.time = in.readLong();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeLong(this.worldAge);
        out.writeLong(this.time);
    }
}

package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

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
public class ServerboundKeepAlivePacket implements Packet {
    private final long pingId;

    public ServerboundKeepAlivePacket(NetInput in) throws IOException {
        this.pingId = in.readLong();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeLong(this.pingId);
    }

}

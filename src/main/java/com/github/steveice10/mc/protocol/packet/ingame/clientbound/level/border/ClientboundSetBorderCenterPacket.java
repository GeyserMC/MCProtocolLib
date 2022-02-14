package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border;

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
public class ClientboundSetBorderCenterPacket implements Packet {
    private final double newCenterX;
    private final double newCenterZ;

    public ClientboundSetBorderCenterPacket(NetInput in) throws IOException {
        this.newCenterX = in.readDouble();
        this.newCenterZ = in.readDouble();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeDouble(this.newCenterX);
        out.writeDouble(this.newCenterZ);
    }
}

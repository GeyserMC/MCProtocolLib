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
public class ClientboundSetBorderLerpSizePacket implements Packet {
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;

    public ClientboundSetBorderLerpSizePacket(NetInput in) throws IOException {
        this.oldSize = in.readDouble();
        this.newSize = in.readDouble();
        this.lerpTime = in.readVarLong();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeDouble(this.oldSize);
        out.writeDouble(this.newSize);
        out.writeVarLong(this.lerpTime);
    }
}

package com.github.steveice10.mc.protocol.packet.ingame.server.world.border;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerSetBorderLerpSizePacket implements Packet {
    private double oldSize;
    private double newSize;
    private long lerpTime;

    @Override
    public void read(NetInput in) throws IOException {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}

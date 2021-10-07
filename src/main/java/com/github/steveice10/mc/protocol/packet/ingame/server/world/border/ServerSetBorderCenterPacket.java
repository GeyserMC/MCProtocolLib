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
public class ServerSetBorderCenterPacket implements Packet {
    private double newCenterX;
    private double newCenterZ;

    @Override
    public void read(NetInput in) throws IOException {
        this.newCenterX = in.readDouble();
        this.newCenterZ = in.readDouble();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeDouble(this.newCenterX);
        out.writeDouble(this.newCenterZ);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

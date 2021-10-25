package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border;

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
public class ClientboundSetBorderSizePacket implements Packet {
    private double size;

    @Override
    public void read(NetInput in) throws IOException {
        this.size = in.readDouble();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeDouble(this.size);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}

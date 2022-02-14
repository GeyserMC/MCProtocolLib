package com.github.steveice10.mc.protocol.packet.ingame.clientbound.title;

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
public class ClientboundSetTitlesAnimationPacket implements Packet {
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public ClientboundSetTitlesAnimationPacket(NetInput in) throws IOException {
        this.fadeIn = in.readInt();
        this.stay = in.readInt();
        this.fadeOut = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.fadeIn);
        out.writeInt(this.stay);
        out.writeInt(this.fadeOut);
    }
}

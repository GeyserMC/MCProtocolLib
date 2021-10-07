package com.github.steveice10.mc.protocol.packet.ingame.server.title;

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
public class ServerSetTitlesAnimationPacket implements Packet {
    private int fadeIn;
    private int stay;
    private int fadeOut;

    @Override
    public void read(NetInput in) throws IOException {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}

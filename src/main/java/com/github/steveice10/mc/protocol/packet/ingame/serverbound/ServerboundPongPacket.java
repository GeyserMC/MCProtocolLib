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
public class ServerboundPongPacket implements Packet {
    private final int id;

    public ServerboundPongPacket(NetInput in) throws IOException {
        this.id = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.id);
    }
}

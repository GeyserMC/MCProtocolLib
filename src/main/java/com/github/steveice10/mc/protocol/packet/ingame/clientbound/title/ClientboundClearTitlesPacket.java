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
public class ClientboundClearTitlesPacket implements Packet {
    private final boolean resetTimes;

    public ClientboundClearTitlesPacket(NetInput in) throws IOException {
        this.resetTimes = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.resetTimes);
    }
}

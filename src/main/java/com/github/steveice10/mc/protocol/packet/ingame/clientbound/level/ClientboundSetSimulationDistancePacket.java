package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

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
public class ClientboundSetSimulationDistancePacket implements Packet {
    private final int simulationDistance;

    public ClientboundSetSimulationDistancePacket(NetInput in) throws IOException {
        this.simulationDistance = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.simulationDistance);
    }
}

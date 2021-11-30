package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSetPassengersPacket implements Packet {
    private final int entityId;
    private final @NonNull int[] passengerIds;

    public ClientboundSetPassengersPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.passengerIds = new int[in.readVarInt()];
        for (int index = 0; index < this.passengerIds.length; index++) {
            this.passengerIds[index] = in.readVarInt();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(this.passengerIds.length);
        for (int entityId : this.passengerIds) {
            out.writeVarInt(entityId);
        }
    }
}
